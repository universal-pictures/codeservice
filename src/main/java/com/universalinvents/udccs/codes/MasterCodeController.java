package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.events.EventConfig;
import com.universalinvents.udccs.events.EventStreamingController;
import com.universalinvents.udccs.events.EventWrapper;
import com.universalinvents.udccs.events.MasterCodeEvent;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.exception.HttpException;
import com.universalinvents.udccs.exception.RecordNotFoundException;
import com.universalinvents.udccs.external.ExternalEstRetailerController;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.ZoneId;
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
    private ExternalEstRetailerController externalEstRetailerController;

    @Autowired
    private PairingRepository pairingRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private StudioRepository studioRepository;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    public ResponseEntity<Page<MasterCode>> getMasterCodes(
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
                    String requestContext,
            @ApiIgnore("Ignored because swagger ui shows the wrong params, " +
                    "instead they are explained in the implicit params")
                    Pageable pageable) {

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

        Page<MasterCode> masterCodes;
        if (params.isEmpty()) {
            masterCodes = masterCodeRepository.findAll(pageable);
        } else {
            Specification<MasterCode> query = specs.get(0);
            for (int i = 1; i < specs.size(); i++) {
                query = Specifications.where(query).and(specs.get(i));
            }
            masterCodes = masterCodeRepository.findAll(query, pageable);
        }


        return new ResponseEntity<Page<MasterCode>>(masterCodes, HttpStatus.OK);
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
            masterCode.setPairing(pairing);

            // Add the pairing object to RetailerCode
            retailerCode.setPairing(pairing);

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
            }

            if (masterCode.getStatus() == MasterCode.Status.EXPIRED) {
                return new ResponseEntity(new ApiError("The selected Master Code is already expired"),
                        HttpStatus.BAD_REQUEST);
            }

            boolean isExpiredRetailerCode;
            try {
                ExternalRetailerCodeStatusResponse status = externalEstRetailerController.status(rc, requestContext,
                        ExternalRetailerCodeStatusResponse.class);
                isExpiredRetailerCode = (status.getStatus().equals(RetailerCode.Status.EXPIRED.toString()));
            } catch (Exception e) {
                return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.CONFLICT);
            }

            if (isExpiredRetailerCode) {
                // Check if the Retailer Code status is out of sync with the true EXPIRED status
                // If so, update the status
                if (rc.getStatus() != RetailerCode.Status.EXPIRED) {
                    rc.setStatus(RetailerCode.Status.EXPIRED);
                    rc.setModifiedOn(modifiedDate);
                    retailerCodeRepository.saveAndFlush(rc);
                }

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

        // Unpair codes
        pairingRepository.delete(masterCode.getPairing());

        // Kill the code with the Retailer
        try {
            externalEstRetailerController.killRetailerCode(retailerCode, requestContext);
        } catch (Exception e) {
            return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.CONFLICT);
        }

        // Delete Retailer Code record
        retailerCodeRepository.delete(retailerCode);

        // Update Master Code status to ISSUED
        Date modifiedOn = new Date();
        masterCode.setStatus(MasterCode.Status.ISSUED);
        masterCode.setModifiedOn(modifiedOn);
        masterCode.setPairing(null);
        masterCodeRepository.saveAndFlush(masterCode);

        // Send a 'masterCodeUnpaired' event
        MasterCodeEvent event = new MasterCodeEvent(masterCode);
        EventWrapper eventWrapper = new EventWrapper(
                "master-code", "masterCodeUnpaired", event, requestContext, eventConfig.getSchemaVersion());
        eventStreamingController.putRecord(eventWrapper.toString());

        // Success
        return new ResponseEntity<>(masterCode, HttpStatus.OK);
    }

    private RetailerCode fetchAndSaveRetailerCode(Content content, String format,
                                                  Retailer retailer, String requestContext)
            throws RecordNotFoundException, HttpException {
        ExternalRetailerCodeResponse externalRc = externalEstRetailerController.fetchRetailerCode(
                content, retailer, requestContext);

        RetailerCode retailerCode = new RetailerCode(
                externalRc.getCode(), content, format, RetailerCode.Status.PAIRED, retailer,
                Date.from(externalRc.getExpiresOn().atZone(ZoneId.systemDefault()).toInstant()));

        return retailerCodeRepository.save(retailerCode);
    }

}
