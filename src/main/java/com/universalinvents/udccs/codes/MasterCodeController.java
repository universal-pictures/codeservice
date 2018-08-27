package com.universalinvents.udccs.codes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.events.EventConfig;
import com.universalinvents.udccs.events.EventStreamingController;
import com.universalinvents.udccs.events.EventWrapper;
import com.universalinvents.udccs.events.MasterCodeEvent;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.exception.RecordNotFoundException;
import com.universalinvents.udccs.external.ExternalRetailerCodeRequest;
import com.universalinvents.udccs.external.ExternalRetailerCodeResponse;
import com.universalinvents.udccs.external.ExternalRetailerCodeStatusResponse;
import com.universalinvents.udccs.pairings.PairMasterCodeRequest;
import com.universalinvents.udccs.pairings.Pairing;
import com.universalinvents.udccs.pairings.PairingRepository;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import com.universalinvents.udccs.utilities.ApiDefinitions;
import com.universalinvents.udccs.utilities.CCFUtility;
import com.universalinvents.udccs.utilities.SqlCriteria;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EventStreamingController eventStreamingController;

    @Autowired
    private EventConfig eventConfig;

    @CrossOrigin
    @ApiOperation(value = "Create a Master Code",
            notes = "A Master Code is generated for future redemption of its related content.")
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
                                                               CreateMasterCodeRequest request,
                                                       @RequestHeader(value = "Request-Context", required = false)
                                                       @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                               String requestContext) {

        Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.BAD_REQUEST);

        ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."),
                    HttpStatus.BAD_REQUEST);

        App app = appRepository.findOne(request.getAppId());
        if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        // Ensure the given app is related to the given referral partner
        if (!referralPartner.getApps().contains(app)) {
            return new ResponseEntity(new ApiError("App id " + request.getAppId() + " is not related with " +
                    "Referral Partner id " + request.getPartnerId() + "."), HttpStatus.BAD_REQUEST);
        }

        String code = CCFUtility.generateCode(content.getStudio().getCodePrefix());
        MasterCode masterCode = new MasterCode(code, request.getFormat(), request.getCreatedBy(), new Date(),
                request.getExpiresOn(), referralPartner, app, content, MasterCode.Status.ISSUED,
                request.getExternalId());
        masterCodeRepository.save(masterCode);

        // Send a 'masterCodeIssued' event
        MasterCodeEvent event = new MasterCodeEvent(masterCode);
        EventWrapper eventWrapper = new EventWrapper(
                "master-code", "masterCodeIssued", event, requestContext, eventConfig.getSchemaVersion());
        eventStreamingController.putRecord(eventWrapper.toString());

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
    }

//    @CrossOrigin
//    @ApiOperation(value = "Ingest a Master Code",
//                  notes = "Use this endpoint if you need to ingest codes from an external source. Master Codes " +
//                  "may only be ingested if a matching Retailer Code has already been ingested. Master Codes " +
//                  "will be given an UNALLOCATED status and will be utilized by future *POST /api/codes/master* " +
//                  "calls where *create* is 'false'.")
//    @ResponseStatus(value = HttpStatus.CREATED)
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Created", response = MasterCode.class),
//            @ApiResponse(code = 400, message = "Specified Content, Referral Partner, or App Not Found",
//                         response = ApiError.class),
//            @ApiResponse(code = 409, message = "Master Code already exists", response = ApiError.class)
//    })
//    @RequestMapping(method = RequestMethod.POST,
//                    value = "/{code}",
//                    produces = "application/json")
//    public ResponseEntity<MasterCode> ingestMasterCode(@PathVariable
//                                                           @ApiParam(value = "The Master Code to ingest",
//                                                                     required = true)
//                                                                   String code,
//                                                       @RequestBody
//                                                       @ApiParam(value = "Provide properties for the Master Code.",
//                                                                 required = true)
//                                                               IngestMasterCodeRequest request) {
//        // See if the code already exists and error if it does
//        MasterCode mc = masterCodeRepository.findOne(code);
//        if (mc != null) {
//            return new ResponseEntity(new ApiError("Master Code already exists"), HttpStatus.CONFLICT);
//        }
//
//        // See if there's a matching Retailer Code and error if not
//        RetailerCode retailerCode = retailerCodeRepository.findOne(code);
//        if (retailerCode == null) {
//            return new ResponseEntity(new ApiError("Matching Retailer Code not found.  Unable to ingest " +
//                code + " as a Master Code until it's ingested as a Retailer Code first."), HttpStatus.BAD_REQUEST);
//        }
//
//        Content content = contentRepository.findOne(request.getContentId());
//        if (content == null)
//            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.BAD_REQUEST);
//
//        ReferralPartner referralPartner = null;
//        if (request.getPartnerId() != null) {
//            referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
//            if (referralPartner == null)
//                return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."),
//                                          HttpStatus.BAD_REQUEST);
//        }
//
//        App app = null;
//        if (request.getAppId() != null) {
//            app = appRepository.findOne(request.getAppId());
//            if (app == null)
//                return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.BAD_REQUEST);
//        }
//
//        MasterCode masterCode = new MasterCode(code, request.getFormat(), request.getCreatedBy(), new Date(),
//                                               referralPartner, app, content, MasterCode.Status.UNALLOCATED, null);
//        masterCodeRepository.save(masterCode);
//        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
//    }

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
                                                            String code,
                                                    @RequestHeader(value = "Request-Context", required = false)
                                                    @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                            String requestContext) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Try to expire an unredeemed Master Code",
            notes = "If the Master Code hasn't reached its expiration date, then this method will return a " +
                    "304 Not Modified.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully expired", response = MasterCode.class),
            @ApiResponse(code = 304, message = "This Master Code has not reached its expiration date", response = ApiError.class),
            @ApiResponse(code = 404, message = "Specified Master Code Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Master Code has an incompatible status", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/{code}/expire", produces = "application/json")
    @Transactional
    public ResponseEntity<MasterCode> expireCode(@PathVariable String code,
                                                 @RequestHeader(value = "Request-Context", required = false)
                                                 @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                         String requestContext) {

        // Get the MasterCode object
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null) {
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);
        }

        // Ensure the MasterCode is not already redeemed
        if (masterCode.getStatus() == MasterCode.Status.REDEEMED) {
            return new ResponseEntity(new ApiError("Master Code with a status of " + masterCode.getStatus() +
                    " can't be expired."),
                    HttpStatus.BAD_REQUEST);
        }

        // Check if we're past our expiration date.  If so, set our status appropriately.
        if (masterCode.getExpiresOn() != null && masterCode.getExpiresOn().compareTo(new Date()) < 0) {
            masterCode.setStatus(MasterCode.Status.EXPIRED);
            masterCode.setModifiedOn(new Date());
            masterCodeRepository.saveAndFlush(masterCode);
            return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
        }

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.NOT_MODIFIED);
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
                    Date modifiedOnBefore,
            @ApiParam(value = "Master Codes that expire after the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "expireAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date expiredOnAfter,
            @ApiParam(value = "Master Codes that expire before the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "expireBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date expiredOnBefore,
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

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
        if (expiredOnAfter != null) {
            params.add(new SqlCriteria("expiredOn", ">", expiredOnAfter));
        }
        if (expiredOnBefore != null) {
            params.add(new SqlCriteria("expiredOn", "<", expiredOnBefore));
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
    @ApiOperation(value = "Update the Content ID related to this Master Code")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Master Code was not modified", response = MasterCode.class),
            @ApiResponse(code = 400, message = "Content related to Master Code is not found", response = ApiError.class),
            @ApiResponse(code = 404, message = "Master Code is Not Found", response = ApiError.class),
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "/{code}", produces = "application/json")
    public ResponseEntity<MasterCode> updateMasterCode(
            @PathVariable
            @ApiParam(value = "The Master Code to update", required = true)
                    String code,
            @RequestBody(required = false)
            @ApiParam(value = "Provide updated properties for the Master Code")
                    UpdateMasterCodeRequest request,
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        // Get existing MasterCode record
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        // US-273: Don't allow any update if its current status is REDEEMED
        if (masterCode.getStatus().equals(MasterCode.Status.REDEEMED)) {
            return new ResponseEntity(new ApiError("Master Code is already REDEEMED and can not be updated."),
                    HttpStatus.NOT_MODIFIED);
        }

        // Update values from request - if set
        boolean isModified = false;
        if (request.getContentId() != null) {
            Content content = contentRepository.findOne(request.getContentId());
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                masterCode.setContent(content);
                isModified = true;
            }
        }

        if (isModified) {
            masterCode.setModifiedOn(new Date());
            masterCodeRepository.save(masterCode);
            return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
        }

        // Nothing was modified.  Just return the found MasterCode.
        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @ApiOperation(value = "Pair Master Code to a Retailer Code",
            notes = "Use this endpoint to associate a Master Code with a Retailer Code.  This endpoint calls " +
                    "the appropriate Retailer Code Service to fetch a valid retailer code for this content.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Master Code is Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Specified Retailer Not Found,\n" +
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
                    PairMasterCodeRequest request,
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.BAD_REQUEST);

        if (!masterCode.getReferralPartner().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("Referral Partner does not have access to selected Retailer."),
                    HttpStatus.BAD_REQUEST);

        // Fetch a new retailer code from the corresponding Retailer Service and insert new Pairing record
        RetailerCode retailerCode = null;

        // Check that we can retrieve a retailer code from this Retailer
        if (retailer.getBaseUrl() == null)
            return new ResponseEntity(new ApiError("The selected retailer does not have baseUrl defined"),
                    HttpStatus.BAD_REQUEST);

        // If the MasterCode isn't already PAIRED or REDEEMED, then fetch a retailer code from the Retailer Service
        Date modifiedDate = new Date();
        boolean isModified = false;
        if (masterCode.getStatus() == MasterCode.Status.ISSUED) {
            try {
                retailerCode = fetchAndSaveRetailerCode(masterCode.getContent(), masterCode.getFormat(), retailer,
                        requestContext);
            } catch (Exception e) {
                return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.CONFLICT);
            }

            Pairing pairing = new Pairing(masterCode, retailerCode, request.getPairedBy(), "ACTIVE");
            pairingRepository.save(pairing);

            // Update the status of the MasterCode
            masterCode.setStatus(MasterCode.Status.PAIRED);
            masterCode.setModifiedOn(modifiedDate);
            masterCodeRepository.saveAndFlush(masterCode);

            isModified = true;
        }

        // If the paired RetailerCode is truly expired and the MasterCode has not expired,
        // then update the Pairing object with a new RetailerCode for the same content with the same Retailer
        else if (masterCode.isPaired()) {
            Pairing pairing = masterCode.getPairing();
            RetailerCode rc = pairing.getRetailerCode();

            // Check if the status is out of date (i.e. the code is already past its expiration date).
            // If so, update our status appropriately.
            if (masterCode.getStatus() != MasterCode.Status.EXPIRED
                    && masterCode.getExpiresOn() != null
                    && masterCode.getExpiresOn().compareTo(new Date()) < 0) {
                masterCode.setStatus(MasterCode.Status.EXPIRED);
                masterCode.setModifiedOn(new Date());
                masterCodeRepository.saveAndFlush(masterCode);
            }

            if (masterCode.getStatus() == MasterCode.Status.EXPIRED) {
                return new ResponseEntity(new ApiError("The selected Master Code is already expired"),
                        HttpStatus.BAD_REQUEST);
            }

            boolean isExpiredRetailerCode;
            try {
                isExpiredRetailerCode = isRetailerCodeExpired(rc.getCode(), retailer.getBaseUrl(), requestContext);
            } catch (Exception e) {
                return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.CONFLICT);
            }

            if (rc.getStatus() == RetailerCode.Status.EXPIRED && isExpiredRetailerCode) {
                // This RetailerCode is truly expired, so pair with a new RetailerCode
                try {
                    retailerCode = fetchAndSaveRetailerCode(masterCode.getContent(), masterCode.getFormat(), retailer,
                            requestContext);
                } catch (Exception e) {
                    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.CONFLICT);
                }

                // Update the pairing record
                pairing.setRetailerCode(retailerCode);
                pairing.setPairedBy(request.getPairedBy());
                pairing.setPairedOn(modifiedDate);
                pairingRepository.saveAndFlush(pairing);

                isModified = true;
            } else if (rc.getStatus() == RetailerCode.Status.EXPIRED) {
                // This RetailerCode isn't truly expired, so update its status back to PAIRED
                rc.setStatus(RetailerCode.Status.PAIRED);
                rc.setModifiedOn(modifiedDate);
                retailerCodeRepository.saveAndFlush(rc);

                isModified = true;
            } else
                return new ResponseEntity(new ApiError("Master Code expressed is already paired."), HttpStatus.CONFLICT);
        }

        // Incompatible status
        else
            return new ResponseEntity(new ApiError("Incompatible status for pairing."), HttpStatus.CONFLICT);

        if (isModified) {
            // Send a 'masterCodePaired' event
            MasterCodeEvent event = new MasterCodeEvent(masterCode);
            EventWrapper eventWrapper = new EventWrapper(
                    "master-code", "masterCodePaired", event, requestContext, eventConfig.getSchemaVersion());
            eventStreamingController.putRecord(eventWrapper.toString());
        }

        // Success
        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Unpair Master Code from its paired Retailer Code",
            notes = "Use this endpoint to unassociate a Master Code from a Retailer Code.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "The current Master Code or Retailer Code status does not support unpairing",
                    response = ApiError.class),
            @ApiResponse(code = 404, message = "Master Code is Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PUT,
            value = "/{code}/unpair",
            produces = "application/json")
    @Transactional
    public ResponseEntity<MasterCode> unpairMasterCode(
            @PathVariable
            @ApiParam(value = "The Master Code to unpair")
                    String code,
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        // Ensure the status of both the Master & Retailer Codes are PAIRED or
        // that the Master Code is PAIRED while the Retailer Code is EXPIRED.
        if (masterCode.getStatus() != MasterCode.Status.PAIRED) {
            return new ResponseEntity(new ApiError("Unable to unpair a Master Code with a status of " + masterCode.getStatus()), HttpStatus.BAD_REQUEST);
        }
        RetailerCode retailerCode = masterCode.getPairing().getRetailerCode();
        if (retailerCode.getStatus() != RetailerCode.Status.PAIRED &&
                retailerCode.getStatus() != RetailerCode.Status.EXPIRED) {
            return new ResponseEntity(new ApiError("Unable to unpair a Retailer Code with a status of " + retailerCode.getStatus()), HttpStatus.BAD_REQUEST);
        }

        // Update Retailer Code status to ZOMBIED
        Date modifiedOn = new Date();
        retailerCode.setStatus(RetailerCode.Status.ZOMBIED);
        retailerCode.setModifiedOn(modifiedOn);
        retailerCodeRepository.save(retailerCode);

        // Unpair codes
        pairingRepository.delete(masterCode.getPairing());

        // Update Master Code status to ISSUED
        masterCode.setStatus(MasterCode.Status.ISSUED);
        masterCode.setModifiedOn(modifiedOn);
        masterCodeRepository.saveAndFlush(masterCode);

        // Send a 'masterCodeUnpaired' event
        MasterCodeEvent event = new MasterCodeEvent(masterCode);
        EventWrapper eventWrapper = new EventWrapper(
                "master-code", "masterCodeUnpaired", event, requestContext, eventConfig.getSchemaVersion());
        eventStreamingController.putRecord(eventWrapper.toString());

        // Success
        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    private boolean isRetailerCodeExpired(String code, String baseUrl, String requestContext)
            throws RecordNotFoundException {
        String url = baseUrl + "/retailerCodes/{code}/status/refresh";
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Request-Context", requestContext);
        HttpEntity entity = new HttpEntity(headers);

        ExternalRetailerCodeStatusResponse status;
        try {
            status = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ExternalRetailerCodeStatusResponse.class, vars).getBody();
            return status.getStatus().equals("EXPIRED");
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    ApiError error = mapper.readValue(e.getResponseBodyAsByteArray(), ApiError.class);
                    throw new RecordNotFoundException(error.getMessage());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    throw new RecordNotFoundException(e.getResponseBodyAsString());
                }
            } else {
                throw e;
            }
        }
    }

    private RetailerCode fetchAndSaveRetailerCode(Content content, String format,
                                                  Retailer retailer, String requestContext)
            throws RecordNotFoundException {
        String url = retailer.getBaseUrl() + "/retailerCodes";
        ExternalRetailerCodeRequest request = new ExternalRetailerCodeRequest(content.getEidr(), null);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Request-Context", requestContext);
        HttpEntity<ExternalRetailerCodeRequest> entity = new HttpEntity<ExternalRetailerCodeRequest>(request, headers);

        ExternalRetailerCodeResponse externalRc;
        try {
            externalRc = restTemplate.exchange(url, HttpMethod.POST, entity,
                    ExternalRetailerCodeResponse.class).getBody();
            RetailerCode retailerCode = new RetailerCode(
                    externalRc.getCode(), content, format, RetailerCode.Status.PAIRED, retailer,
                    Date.from(externalRc.getExpiresOn().atZone(ZoneId.systemDefault()).toInstant()));

            return retailerCodeRepository.save(retailerCode);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    ApiError error = mapper.readValue(e.getResponseBodyAsByteArray(), ApiError.class);
                    throw new RecordNotFoundException(error.getMessage());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    throw new RecordNotFoundException(e.getResponseBodyAsString());
                }
            } else {
                throw e;
            }
        }
    }

//    private RetailerCode getRetailerCode(Content content, String format, Retailer retailer) throws ApiError {
//        // Build a RetailerCode object with the values passed in
//        RetailerCode retailerCode = new RetailerCode();
//
//        // Unpaired RetailerCodes for the given Content & Retailer
//        retailerCode.setContent(content);
//        retailerCode.setRetailer(retailer);
//        retailerCode.setFormat(format);
////        retailerCode.setStatus(RetailerCode.Status.UNALLOCATED);
//
//        // Find all of the matches sorted by their creation date
//        List<RetailerCode> retailerCodes = retailerCodeRepository.findAll(Example.of(retailerCode),
//                                                                          new Sort("createdOn"));
//
//        if (retailerCodes == null || retailerCodes.isEmpty()) {
//            throw new ApiError("Retailer Code not available for selected Content");
//        }
//
//        // Return just the first record found
//        return retailerCodes.get(0);
//    }

//    private MasterCode getMasterCode(Long contentId, String format, MasterCode.Status status)
//            throws IllegalArgumentException {
//        // Build a MasterCode object with the values passed in
//        MasterCode masterCode = new MasterCode();
//
//        Content content = contentRepository.findOne(contentId);
//        if (content == null) {
//            throw new IllegalArgumentException("Content id specified not found.");
//        } else {
//            masterCode.setContent(content);
//        }
//
//        masterCode.setFormat(format);
//        masterCode.setStatus(status);
//
//        // Find all of the matches sorted by their creation date
//        List<MasterCode> masterCodes = masterCodeRepository.findAll(Example.of(masterCode), new Sort("createdOn"));
//
//        // Return just the first record found
//        return masterCodes.get(0);
//    }

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
