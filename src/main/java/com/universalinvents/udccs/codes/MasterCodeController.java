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
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import com.universalinvents.udccs.utilities.CCFUtility;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

            ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
            if (referralPartner == null)
                return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."),
                                          HttpStatus.NOT_FOUND);

            Studio studio = studioRepository.findOne(request.getStudioId());
            if (studio == null)
                return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);

            App app = appRepository.findOne(request.getAppId());
            if (app == null)
                return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

            String code = CCFUtility.generateCode(studio.getCodePrefix());
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

        ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);

        Studio studio = studioRepository.findOne(request.getStudioId());
        if (studio == null)
            return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);

        App app = appRepository.findOne(request.getAppId());
        if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

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
            @RequestParam(name = "status", required = false) String status) {

        // Build a MasterCode object with the values passed in
        MasterCode masterCode = new MasterCode();

        if (partnerId != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(partnerId);
            if (referralPartner == null) {
                return new ResponseEntity(new ApiError("Referral Partner id specified not found."),
                                          HttpStatus.BAD_REQUEST);
            } else {
                masterCode.setReferralPartner(referralPartner);
            }
        }

        if (appId != null) {
            App app = appRepository.findOne(appId);
            if (app == null) {
                return new ResponseEntity(new ApiError("App id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                masterCode.setApp(app);
            }
        }

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                masterCode.setContent(content);
            }
        }

        if (status != null) {
            try {
                masterCode.setStatus(MasterCode.Status.valueOf(status));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity(new ApiError(
                        "Status value not allowed. Please use one of: " + Arrays.asList(MasterCode.Status.values())),
                                          HttpStatus.BAD_REQUEST);
            }
        }

        List<MasterCode> masterCodes = masterCodeRepository.findAll(Example.of(masterCode));
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

        // TODO: Use the following method for getting a retailerCode when we're no longer importing MA codes as both Master & Retailer Codes
        // Get a Retailer Code for the related Content
        //final RetailerCode retailerCode = getRetailerCode(masterCode.getContent(), retailer);

        // HARDCODED!!!  Replace with above later
        // Get a retailerCode with the same value as the masterCode
        final RetailerCode retailerCode = retailerCodeRepository.findOne(masterCode.getCode());
        if (retailerCode == null) {
            return new ResponseEntity(new ApiError("Retailer Code not available for selected Content"),
                                      HttpStatus.NOT_FOUND);
        }

        // Insert new pairings record
        Date modifiedDate = new Date();
        Pairing pairing = new Pairing(masterCode, retailerCode,
                                      request.getPairedBy(), "ACTIVE");
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

    private RetailerCode getRetailerCode(Content content, Retailer retailer) {
        // Build a RetailerCode object with the values passed in
        RetailerCode retailerCode = new RetailerCode();

        // Unpaired RetailerCodes for the given Content & Retailer
        retailerCode.setContent(content);
        retailerCode.setRetailer(retailer);
        retailerCode.setStatus(RetailerCode.Status.UNALLOCATED);

        // Find all of the matches sorted by their creation date
        List<RetailerCode> retailerCodes = retailerCodeRepository.findAll(Example.of(retailerCode),
                                                                          new Sort("createdOn"));

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
