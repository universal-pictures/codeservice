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
import com.universalinvents.udccs.utilities.SqlCriteria;
import io.swagger.annotations.*;
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

@Api(tags = {"Master Code Controller"},
     description = "Operations pertaining to master codes")
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
    @ApiOperation(value = "Obtain a Master Code",
                  notes = "A Master Code is given to a customer for future redemption of its related content. " +
                  "You may either generate a code on the fly or retrieve one that was previously ingested. " +
                  "Here's how that works:\n\n<br/>" +
                  "If *request.create* is *true*:\n" +
                  "1. Generate a new ISSUED code and return it to the user\n\n" +
                  "If *request.create* is *false* (or undefined):\n" +
                  "1. Find an UNALLOCATED code for the given content\n" +
                  "2. Update that code's status to ISSUED\n" +
                  "3. Return the updated code to the user\n\n" +
                  "Additionally, if *request.create* is *true* then the following parameters are required:\n" +
                  "* *request.appId*\n" +
                  "* *request.partnerId*")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = MasterCode.class),
            @ApiResponse(code = 400, message = "Specified Content, Referral Partner, or App Not Found",
                         response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.POST,
                    produces = "application/json")
    @Transactional
    public ResponseEntity<MasterCode> createMasterCode(@RequestBody
                                                       @ApiParam(value = "Provide properties for the Master Code.",
                                                                 required = true)
                                                                   CreateMasterCodeRequest request) {

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
                return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.BAD_REQUEST);

            ReferralPartner referralPartner = null;
            if (request.getPartnerId() != null) {
                referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
                if (referralPartner == null)
                    return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."),
                                              HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(new ApiError("partnerId parameter is required when create is true."),
                                          HttpStatus.BAD_REQUEST);
            }

            App app = null;
            if (request.getAppId() != null) {
                app = appRepository.findOne(request.getAppId());
                if (app == null)
                    return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(new ApiError("appId parameter is required when create is true."),
                                          HttpStatus.BAD_REQUEST);
            }

            String code = CCFUtility.generateCode(content.getStudio().getCodePrefix());
            MasterCode masterCode = new MasterCode(code, request.getFormat(), request.getCreatedBy(), new Date(),
                                                   referralPartner, app, content, MasterCode.Status.ISSUED,
                                                   request.getExternalId());
            masterCodeRepository.save(masterCode);
            return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
        } else {
            try {
                MasterCode masterCode = getMasterCode(request.getContentId(), request.getFormat(),
                                                      MasterCode.Status.UNALLOCATED);
                masterCode.setStatus(MasterCode.Status.ISSUED);
                masterCode.setExternalId(request.getExternalId());
                masterCodeRepository.save(masterCode);
                return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
            } catch (ApiError apiError) {
                return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Ingest a Master Code",
                  notes = "Use this endpoint if you need to ingest codes from an external source. Master Codes " +
                  "may only be ingested if a matching Retailer Code has already been ingested. Master Codes " +
                  "will be given an UNALLOCATED status and will be utilized by future *POST /api/codes/master* " +
                  "calls where *create* is 'false'.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = MasterCode.class),
            @ApiResponse(code = 400, message = "Specified Content, Referral Partner, or App Not Found",
                         response = ApiError.class),
            @ApiResponse(code = 409, message = "Master Code already exists", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.POST,
                    value = "/{code}",
                    produces = "application/json")
    public ResponseEntity<MasterCode> ingestMasterCode(@PathVariable
                                                           @ApiParam(value = "The Master Code to ingest",
                                                                     required = true)
                                                                   String code,
                                                       @RequestBody
                                                       @ApiParam(value = "Provide properties for the Master Code.",
                                                                 required = true)
                                                               IngestMasterCodeRequest request) {
        // See if the code already exists and error if it does
        MasterCode mc = masterCodeRepository.findOne(code);
        if (mc != null) {
            return new ResponseEntity(new ApiError("Master Code already exists"), HttpStatus.CONFLICT);
        }

        // See if there's a matching Retailer Code and error if not
        RetailerCode retailerCode = retailerCodeRepository.findOne(code);
        if (retailerCode == null) {
            return new ResponseEntity(new ApiError("Matching Retailer Code not found.  Unable to ingest " +
                code + " as a Master Code until it's ingested as a Retailer Code first."), HttpStatus.BAD_REQUEST);
        }

        Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.BAD_REQUEST);

        ReferralPartner referralPartner = null;
        if (request.getPartnerId() != null) {
            referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
            if (referralPartner == null)
                return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."),
                                          HttpStatus.BAD_REQUEST);
        }

        App app = null;
        if (request.getAppId() != null) {
            app = appRepository.findOne(request.getAppId());
            if (app == null)
                return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.BAD_REQUEST);
        }

        MasterCode masterCode = new MasterCode(code, request.getFormat(), request.getCreatedBy(), new Date(),
                                               referralPartner, app, content, MasterCode.Status.UNALLOCATED, null);
        masterCodeRepository.save(masterCode);
        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
    }

//    @CrossOrigin
//    @ApiOperation(value = "Update the status of a Master Code",
//                  notes = "Master Codes can have one of the following status values:\n\n" +
//                  "| Status      | Description                                            |\n" +
//                  "| ----------- | ------------------------------------------------------ |\n" +
//                  "| UNALLOCATED | Code has been ingested and is available for a customer |\n" +
//                  "| ISSUED      | Code has been given to a customer                      |\n" +
//                  "| PAIRED      | Code has been related with a Retailer Code             |\n" +
//                  "| REDEEMED    | Code has been redeemed at the Retailer                 |\n")
//    @ResponseStatus(value = HttpStatus.OK)
//    @ApiResponses(value = {
//            @ApiResponse(code = 304, message = "Master Code was not modified", response = MasterCode.class),
//            @ApiResponse(code = 404, message = "Master Code is Not Found", response = ApiError.class),
//            @ApiResponse(code = 400, message = "Specified status not allowed", response = ApiError.class)
//    })
//    @RequestMapping(method = RequestMethod.PATCH,
//                    value = "/{code}",
//                    produces = "application/json")
//    public ResponseEntity<MasterCode> updateMasterCode(
//            @PathVariable
//            @ApiParam(value = "The Master Code to update")
//                    String code,
//            @RequestBody
//            @ApiParam(value = "The new status value")
//                    String newStatus) {
//        // Get existing MasterCode record
//        MasterCode masterCode = masterCodeRepository.findOne(code);
//        if (masterCode == null)
//            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);
//
//        try {
//            masterCode.setStatus(MasterCode.Status.valueOf(newStatus));
//            masterCode.setModifiedOn(new Date());
//            masterCodeRepository.save(masterCode);
//            return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity(new ApiError("Status value not allowed. Please use one of: " +
//                                                           Arrays.asList(MasterCode.Status.values())),
//                                      HttpStatus.BAD_REQUEST);
//        }
//    }

    @CrossOrigin
    @ApiOperation(value = "Get Master Code information for a given code")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET,
                    value = "/{code}",
                    produces = "application/json")
    public ResponseEntity<MasterCode> getMasterCode(@PathVariable
                                                        @ApiParam(value = "The Master Code to retrieve")
                                                                String code) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Master Codes",
                  notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                          "to filter the results (AND as opposed to OR)")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Specified Content, Referral Partner, or App Not Found",
                         response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET,
                    produces = "application/json")
    public ResponseEntity<List<MasterCode>> getMasterCodes(
            @ApiParam(value = "Referral Partner related to Master Codes.")
            @RequestParam(name = "partnerId", required = false)
                    Long partnerId,
            @ApiParam(value = "App related to Master Codes.")
            @RequestParam(name = "appId", required = false)
                    Long appId,
            @ApiParam(value = "Content related to Master Codes.")
            @RequestParam(name = "contentId", required = false)
                    Long contentId,
            @ApiParam(value = "Studio related to Master Code Content.")
            @RequestParam(name = "studioId", required = false)
                    Long studioId,
            @ApiParam(value = "Master Codes with this status.")
            @RequestParam(name = "status", required = false)
                    String status,
            @ApiParam(value = "Master Code(s) with this external id.")
            @RequestParam(name = "externalId", required = false)
                    String externalId,
            @ApiParam(value = "Master Codes created after the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "createdAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date createdOnAfter,
            @ApiParam(value = "Master Codes created before the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "createdBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date createdOnBefore,
            @ApiParam(value = "Master Codes modified after the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "modifiedAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date modifiedOnAfter,
            @ApiParam(value = "Master Codes modified before the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "modifiedBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date modifiedOnBefore) {

        ArrayList<SqlCriteria> params = new ArrayList<SqlCriteria>();

        if (partnerId != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(partnerId);
            if (referralPartner == null) {
                return new ResponseEntity(new ApiError("Referral Partner id specified not found."),
                                          HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("referralPartner", ":", partnerId));
            }
        }

        if (appId != null) {
            App app = appRepository.findOne(appId);
            if (app == null) {
                return new ResponseEntity(new ApiError("App id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("app", ":", appId));
            }
        }

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("content", ":", contentId));
            }
        }

        if (studioId != null) {
            Studio studio = studioRepository.findOne(studioId);
            if (studio == null) {
                return new ResponseEntity(new ApiError("Studio id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("content.studio", ":", studioId));
            }
        }

        if (status != null) {
            try {
                params.add(new SqlCriteria("status", ":", MasterCode.Status.valueOf(status)));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity(new ApiError(
                        "Status value not allowed. Please use one of: " + Arrays.asList(MasterCode.Status.values())),
                                          HttpStatus.BAD_REQUEST);
            }
        }

        if (externalId != null) {
            params.add(new SqlCriteria("externalId", ":", externalId));
        }

        if (createdOnAfter != null) {
            params.add(new SqlCriteria("createdOn", ">", createdOnAfter));
        }
        if (createdOnBefore != null) {
            params.add(new SqlCriteria("createdOn", "<", createdOnBefore));
        }
        if (modifiedOnAfter != null) {
            params.add(new SqlCriteria("modifiedOn", ">", modifiedOnAfter));
        }
        if (modifiedOnBefore != null) {
            params.add(new SqlCriteria("modifiedOn", "<", modifiedOnBefore));
        }

        List<Specification<MasterCode>> specs = new ArrayList<>();
        for (SqlCriteria param : params) {
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
    @ApiOperation(value = "Pair Master Code to a Retailer Code",
                  notes = "Use this endpoint to associate a Master Code with a Retailer Code. This operates " +
                  "differently depending on whether the Master Code was ingested or dynamically generated:\n\n<br/>" +
                  "**If Ingested:**\n\n" +
                  "* Pair with the matching Retailer Code\n\n" +
                  "**If Generated:**\n\n" +
                  "* Pair with a random Retailer Code for the same Content and format as the Master Code")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Master Code is Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Specified Retailer Not Found,\n" +
                         "Retailer does not have requested Content,\n" +
                         "Referral Partner does not have access to Retailer", response = ApiError.class),
            @ApiResponse(code = 409, message = "Master Code is already paired OR\n" +
                    "Retailer Code unavailable for Content", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PUT,
                    value = "/{code}/pair",
                    produces = "application/json")
    @Transactional
    public ResponseEntity<MasterCode> pairMasterCode(
            @PathVariable
            @ApiParam(value = "The Master Code to pair with")
                    String code,
            @RequestBody
            @ApiParam(value = "Additional request properties")
                    PairMasterCodeRequest request) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        if (masterCode.isPaired())
            return new ResponseEntity(new ApiError("Master Code expressed is already paired."), HttpStatus.CONFLICT);

        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.BAD_REQUEST);

        if (!masterCode.getContent().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("Retailer does not have requested Content."), HttpStatus.BAD_REQUEST);

        if (!masterCode.getReferralPartner().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("Referral Partner does not have access to selected Retailer."),
                                      HttpStatus.BAD_REQUEST);

        // First:
        // Try to get a retailerCode with the same value as the masterCode
        RetailerCode retailerCode = retailerCodeRepository.findOne(masterCode.getCode());
        if (retailerCode == null) {
            // Second:
            // If one wasn't found, get a random retailerCode for the related Content
            try {
                retailerCode = getRetailerCode(masterCode.getContent(), masterCode.getFormat(), retailer);
            } catch (ApiError apiError) {
                return new ResponseEntity(apiError, HttpStatus.CONFLICT);
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

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    private RetailerCode getRetailerCode(Content content, String format, Retailer retailer) throws ApiError {
        // Build a RetailerCode object with the values passed in
        RetailerCode retailerCode = new RetailerCode();

        // Unpaired RetailerCodes for the given Content & Retailer
        retailerCode.setContent(content);
        retailerCode.setRetailer(retailer);
        retailerCode.setFormat(format);
//        retailerCode.setStatus(RetailerCode.Status.UNALLOCATED);

        // Find all of the matches sorted by their creation date
        List<RetailerCode> retailerCodes = retailerCodeRepository.findAll(Example.of(retailerCode),
                                                                          new Sort("createdOn"));

        if (retailerCodes == null || retailerCodes.isEmpty()) {
            throw new ApiError("Retailer Code not available for selected Content");
        }

        // Return just the first record found
        return retailerCodes.get(0);
    }

    private MasterCode getMasterCode(Long contentId, String format, MasterCode.Status status) throws ApiError {
        // Build a MasterCode object with the values passed in
        MasterCode masterCode = new MasterCode();

        Content content = contentRepository.findOne(contentId);
        if (content == null) {
            throw new ApiError("Content id specified not found.");
        } else {
            masterCode.setContent(content);
        }

        masterCode.setFormat(format);

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
//            return new ResponseEntity(new ApiError("Master Code expressed is already redeemed."), HttpStatus
// .CONFLICT);
//
//        masterCode.setRedeemedOn(new Date());
//        masterCode.setRedeemedBy(request.getRedeemedBy());
//
//        masterCodeRepository.save(masterCode);
//
//        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
//    }

}
