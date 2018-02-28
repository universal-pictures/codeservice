package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.pairings.PairMasterCodeRequest;
import com.universalinvents.udccs.pairings.Pairing;
import com.universalinvents.udccs.pairings.PairingRepository;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import com.universalinvents.udccs.studios.StudioRepository;
import com.universalinvents.udccs.utilities.CCFUtility;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/codes/master")
public class MasterCodeController {
    @Autowired
    private MasterCodeRepository masterCodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private RetailerCodeRepository retailerCodeRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private PairingRepository pairingRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private StudioRepository studioRepository;

    @CrossOrigin
    @ApiOperation("Create a Master Code")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @Transactional
    public ResponseEntity<MasterCode> createMasterCode(@RequestBody CreateMasterCodeRequest request) {

        //
        // If request.create is true:
        //   1) Simply create a new ISSUED code and return to the user
        //
        // If request.create is false:
        //   1) Find an UNALLOCATED code for the given content
        //   2) Update that code's status to ISSUED
        //   3) Return the updated code to the user
        //
        if (request.getCreate()) {
            Content content = contentRepository.findOne(request.getContentId());
            if (content == null)
                return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

            ReferralPartner referralPartner = null;
            if (request.getPartnerId() != null) {
                referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
                if (referralPartner == null)
                    return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);
            }

            App app = null;
            if (request.getAppId() != null) {
                app = appRepository.findOne(request.getAppId());
                if (app == null) return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);
            }

            String code = CCFUtility.generateCode(content.getStudio().getCodePrefix());
            MasterCode masterCode = new MasterCode(code, request.getCreatedBy(), new Date(), referralPartner, app,
                                                   content, MasterCode.Status.ISSUED);
            masterCodeRepository.save(masterCode);
            return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
        } else {
            try {
                MasterCode masterCode = getMasterCode(request.getContentId(), MasterCode.Status.UNALLOCATED);
                masterCode.setStatus(MasterCode.Status.ISSUED);
                masterCodeRepository.save(masterCode);
                return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
            } catch (ApiError apiError) {
                return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @CrossOrigin
    @ApiOperation("Ingest a Master Code")
    @RequestMapping(method = RequestMethod.POST, value = "/{code}", produces = "application/json")
    public ResponseEntity<MasterCode> ingestMasterCode(@PathVariable String code,
                                                       @RequestBody IngestMasterCodeRequest request) {
        Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        ReferralPartner referralPartner = null;
        if (request.getPartnerId() != null) {
            referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
            if (referralPartner == null)
                return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);
        }

        App app = null;
        if (request.getAppId() != null) {
            app = appRepository.findOne(request.getAppId());
            if (app == null) return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);
        }

        MasterCode masterCode = new MasterCode(code, request.getCreatedBy(), new Date(), referralPartner, app, content,
                                               MasterCode.Status.UNALLOCATED);
        masterCodeRepository.save(masterCode);
        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Update the status of a Master Code")
    @RequestMapping(method = RequestMethod.PATCH, value = "/{code}", produces = "application/json")
    public ResponseEntity<MasterCode> updateMasterCode(@PathVariable String code, @RequestBody String newStatus) {
        // Get existing MasterCode record
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        masterCode.setStatus(MasterCode.Status.valueOf(newStatus));
        masterCode.setModifiedOn(new Date());

        masterCodeRepository.save(masterCode);
        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Master Code information for a given code")
    @RequestMapping(method = RequestMethod.GET, value = "/{code}", produces = "application/json")
    public ResponseEntity<MasterCode> getMasterCode(@PathVariable String code) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Master Codes")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<MasterCode>> getMasterCodes(
            @RequestParam(name = "partnerId", required = false) Long partnerId,
            @RequestParam(name = "appId", required = false) Long appId,
            @RequestParam(name = "contentId", required = false) Long contentId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "createdOnAfter", required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME) Date createdOnAfter,
            @RequestParam(name = "createdOnBefore", required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME) Date createdOnBefore,
            @RequestParam(name = "modifiedOnAfter", required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME) Date modifiedOnAfter,
            @RequestParam(name = "modifiedOnBefore", required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME) Date modifiedOnBefore) {

        ArrayList<CodeCriteria> params = new ArrayList<CodeCriteria>();

        if (partnerId != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(partnerId);
            if (referralPartner == null) {
                return new ResponseEntity(new ApiError("Referral Partner id specified not found."),
                                          HttpStatus.BAD_REQUEST);
            } else {
                params.add(new CodeCriteria("referralPartner", ":", partnerId));
            }
        }

        if (appId != null) {
            App app = appRepository.findOne(appId);
            if (app == null) {
                return new ResponseEntity(new ApiError("App id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new CodeCriteria("app", ":", appId));
            }
        }

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new CodeCriteria("content", ":", contentId));
            }
        }

        if (status != null) {
            try {
                params.add(new CodeCriteria("status", ":", MasterCode.Status.valueOf(status)));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity(new ApiError(
                        "Status value not allowed. Please use one of: " + Arrays.asList(MasterCode.Status.values())),
                                          HttpStatus.BAD_REQUEST);
            }
        }

        if (createdOnAfter != null) {
            params.add(new CodeCriteria("createdOn", ">", createdOnAfter));
        }
        if (createdOnBefore != null) {
            params.add(new CodeCriteria("createdOn", "<", createdOnBefore));
        }
        if (modifiedOnAfter != null) {
            params.add(new CodeCriteria("modifiedOn", ">", modifiedOnAfter));
        }
        if (modifiedOnBefore != null) {
            params.add(new CodeCriteria("modifiedOn", "<", modifiedOnBefore));
        }

        List<Specification<MasterCode>> specs = new ArrayList<>();
        for (CodeCriteria param : params) {
            specs.add(new MasterCodeSpecification(param));
        }

        List<MasterCode> masterCodes = new ArrayList<MasterCode>();
        if (params.isEmpty()) {
            masterCodes = masterCodeRepository.findAll();
        } else {
            Specification<MasterCode> query = specs.get(0);
            for (int i = 1; i < specs.size(); i++) {
                query = Specifications.where(query).and(specs.get(i));
            }
            masterCodes = masterCodeRepository.findAll(query);
        }


        return new ResponseEntity<List<MasterCode>>(masterCodes, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Pair Master Code to a Retailer Code")
    @RequestMapping(method = RequestMethod.PUT, value = "/{code}/pair", produces = "application/json")
    @Transactional
    public ResponseEntity<MasterCode> pairMasterCode(@PathVariable String code,
                                                     @RequestBody PairMasterCodeRequest request) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        if (masterCode.isPaired())
            return new ResponseEntity(new ApiError("Master Code expressed is already paired."), HttpStatus.CONFLICT);

        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        if (!masterCode.getContent().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("Retailer does not have requested Content."), HttpStatus.NOT_FOUND);

        if (!masterCode.getReferralPartner().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("ReferralPartner does not have access to selected Retailer."),
                                      HttpStatus.NOT_FOUND);

        // First:
        // Try to get a retailerCode with the same value as the masterCode
        RetailerCode retailerCode = retailerCodeRepository.findOne(masterCode.getCode());
        if (retailerCode == null) {
            // Second:
            // If one wasn't found, get a random retailerCode for the related Content
            try {
                retailerCode = getRetailerCode(masterCode.getContent(), retailer);
            } catch (ApiError apiError) {
                return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
            }
        }

        // Insert new pairings record
        Date modifiedDate = new Date();
        Pairing pairing = new Pairing(masterCode, retailerCode, request.getPairedBy(), "ACTIVE");
        pairingRepository.saveAndFlush(pairing);

        // Update the status of both MasterCode and RetailerCode
        masterCode.setStatus(MasterCode.Status.PAIRED);
        masterCode.setModifiedOn(modifiedDate);
        masterCodeRepository.saveAndFlush(masterCode);
        retailerCode.setStatus(RetailerCode.Status.PAIRED);
        retailerCode.setModifiedOn(modifiedDate);
        retailerCodeRepository.saveAndFlush(retailerCode);

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
    }

    private RetailerCode getRetailerCode(Content content, Retailer retailer) throws ApiError {
        // Build a RetailerCode object with the values passed in
        RetailerCode retailerCode = new RetailerCode();

        // Unpaired RetailerCodes for the given Content & Retailer
        retailerCode.setContent(content);
        retailerCode.setRetailer(retailer);
        retailerCode.setStatus(RetailerCode.Status.UNALLOCATED);

        // Find all of the matches sorted by their creation date
        List<RetailerCode> retailerCodes = retailerCodeRepository.findAll(Example.of(retailerCode),
                                                                          new Sort("createdOn"));

        if (retailerCodes == null || retailerCodes.isEmpty()) {
            throw new ApiError("Retailer Code not available for selected Content");
        }

        // Return just the first record found
        return retailerCodes.get(0);
    }

    private MasterCode getMasterCode(Long contentId, MasterCode.Status status) throws ApiError {
        // Build a MasterCode object with the values passed in
        MasterCode masterCode = new MasterCode();

        Content content = contentRepository.findOne(contentId);
        if (content == null) {
            throw new ApiError("Content id specified not found.");
        } else {
            masterCode.setContent(content);
        }

        try {
            masterCode.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new ApiError(
                    "Status value not allowed. Please use one of: " + Arrays.asList(MasterCode.Status.values()));
        }

        // Find all of the matches sorted by their creation date
        List<MasterCode> masterCodes = masterCodeRepository.findAll(Example.of(masterCode), new Sort("createdOn"));

        // Return just the first record found
        return masterCodes.get(0);
    }

//    @CrossOrigin
//    @ApiOperation("Alter Master Code redemption status")
//    @RequestMapping(method = RequestMethod.PUT, value = "/{code}/redeem")
//    public ResponseEntity<MasterCode> redeemMasterCode(@PathVariable String code,
//                                                       @RequestBody RedeemMasterCodeRequest request) {
//        MasterCode masterCode = masterCodeRepository.findOne(code);
//        if (masterCode == null)
//            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);
//
//        if (masterCode.isRedeemed())
//            return new ResponseEntity(new ApiError("Master Code expressed is already redeemed."), HttpStatus.CONFLICT);
//
//        masterCode.setRedeemedOn(new Date());
//        masterCode.setRedeemedBy(request.getRedeemedBy());
//
//        masterCodeRepository.save(masterCode);
//
//        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
//    }

}
