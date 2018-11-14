package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.events.EventConfig;
import com.universalinvents.udccs.events.EventStreamingController;
import com.universalinvents.udccs.events.EventWrapper;
import com.universalinvents.udccs.events.MasterCodeEvent;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.exception.HttpException;
import com.universalinvents.udccs.external.ExternalEstRetailerController;
import com.universalinvents.udccs.external.ExternalRetailerCodeResponse;
import com.universalinvents.udccs.external.ExternalRetailerCodeStatusResponse;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import com.universalinvents.udccs.utilities.ApiDefinitions;
import com.universalinvents.udccs.utilities.SqlCriteria;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(tags = {"Retailer Code Controller"},
        description = "Operations pertaining to retailer codes")
@RestController
@RequestMapping("/api/codes/retailer")
public class RetailerCodeController {
    @Autowired
    private RetailerCodeRepository retailerCodeRepository;

    @Autowired
    private MasterCodeRepository masterCodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private ExternalEstRetailerController externalEstRetailerController;

    @Autowired
    private EventStreamingController eventStreamingController;

    @Autowired
    private EventConfig eventConfig;

    private final Logger log = LoggerFactory.getLogger(RetailerCodeController.class);

    /* Ingesting retailer codes will now be handled by that retailer's specific code generation service
    *
    @CrossOrigin
    @ApiOperation(value = "Ingest a Retailer Code",
                  notes = "Use this endpoint to ingest codes from an external source. Retailer Codes " +
                          "will be given an UNALLOCATED status and will be utilized by future " +
                          "*POST /api/codes/master/{code}/pair* calls.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = RetailerCode.class),
            @ApiResponse(code = 400, message = "Specified Content or Retailer Not Found",
                         response = ApiError.class),
            @ApiResponse(code = 409, message = "Retailer Code already exists", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/{code}", produces = "application/json")
    public ResponseEntity<RetailerCode> ingestRetailerCode(
            @PathVariable
            @ApiParam(value = "The Retailer Code to ingest", required = true)
                    String code,
            @RequestBody
            @Valid
            @ApiParam(value = "Provide properties for the Retailer Code.", required = true)
                    RetailerCodeRequest request) {

        // See if the code already exists and error if it does
        RetailerCode rc = retailerCodeRepository.findOne(code);
        if (rc != null) {
            return new ResponseEntity(new ApiError("Retailer code already exists"), HttpStatus.CONFLICT);
        }

        final Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.BAD_REQUEST);

        final Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.BAD_REQUEST);

        final RetailerCode retailerCode = new RetailerCode(code, content, request.getFormat(),
                                                           RetailerCode.Status.UNALLOCATED, retailer, null);
        return new ResponseEntity<RetailerCode>(retailerCodeRepository.save(retailerCode), HttpStatus.CREATED);
    }
    */

    @CrossOrigin
    @ApiOperation(value = "Redeem a Retailer Code and its paired Master Code")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully redeemed", response = RetailerCode.class),
            @ApiResponse(code = 404, message = "Specified Retailer Code Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Retailer Code or paired Master Code have an incompatible status. " +
                    "Both must be PAIRED to redeem.", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/{code}/redeem", produces = "application/json")
    @Transactional
    public ResponseEntity<RetailerCode> redeemCode(@PathVariable String code,
                                                   @RequestHeader(value = "Request-Context", required = false)
                                                   @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                           String requestContext) {

        // Get the RetailerCode object
        RetailerCode retailerCode = retailerCodeRepository.findOne(code);
        if (retailerCode == null) {
            return new ResponseEntity(new ApiError("Retailer Code expressed is not found."), HttpStatus.NOT_FOUND);
        }

        // Ensure the RetailerCode has a PAIRED status
        if (retailerCode.getStatus() != RetailerCode.Status.PAIRED) {
            return new ResponseEntity(new ApiError("Retailer Code with a status of " + retailerCode.getStatus() +
                    " can't be redeemed.  It must be " +
                    RetailerCode.Status.PAIRED + " to redeem."),
                    HttpStatus.BAD_REQUEST);
        }

        // Get the paired MasterCode object
        List<Specification<MasterCode>> specs = new ArrayList<>();
        specs.add(new MasterCodeSpecification(new SqlCriteria("pairing.retailer_code", ":",
                retailerCode.getCode())));
        MasterCode masterCode = masterCodeRepository.findOne(specs.get(0));

        // Ensure the MasterCode has a PAIRED status
        if (masterCode.getStatus() != MasterCode.Status.PAIRED) {
            return new ResponseEntity(new ApiError("Master Code with a status of " + masterCode.getStatus() +
                    " can't be redeemed.  It must be " +
                    MasterCode.Status.PAIRED + " to redeem."),
                    HttpStatus.BAD_REQUEST);
        }

        // Update RetailerCode status to REDEEMED
        Date modifiedDate = new Date();
        retailerCode.setStatus(RetailerCode.Status.REDEEMED);
        retailerCode.setModifiedOn(modifiedDate);
        retailerCodeRepository.saveAndFlush(retailerCode);

        // Update MasterCode status to REDEEMED
        masterCode.setStatus(MasterCode.Status.REDEEMED);
        masterCode.setModifiedOn(modifiedDate);
        masterCodeRepository.saveAndFlush(masterCode);

        // Update EST Inventory Retailer Code status to REDEEMED
        externalEstRetailerController.redeem(retailerCode, requestContext, ExternalRetailerCodeResponse.class);

        // Send a 'masterCodeRedeemed' event
        MasterCodeEvent event = new MasterCodeEvent(masterCode);
        EventWrapper eventWrapper = new EventWrapper(
                "master-code", "masterCodeRedeemed", event, requestContext, eventConfig.getSchemaVersion());
        eventStreamingController.putRecord(eventWrapper.toString());

        return new ResponseEntity<>(retailerCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Try to expire an unredeemed Retailer Code",
            notes = "If the Retailer Code hasn't reached its expiration date, then this method will return a " +
                    "304 Not Modified.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully expired", response = RetailerCode.class),
            @ApiResponse(code = 304, message = "This Retailer Code has not reached its expiration date", response = ApiError.class),
            @ApiResponse(code = 404, message = "Specified Retailer Code Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Retailer Code has an incompatible status", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/{code}/expire", produces = "application/json")
    @Transactional
    public ResponseEntity<RetailerCode> expireCode(@PathVariable String code,
                                                   @RequestHeader(value = "Request-Context", required = false)
                                                   @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                           String requestContext) {

        // Get the RetailerCode object
        RetailerCode retailerCode = retailerCodeRepository.findOne(code);
        if (retailerCode == null) {
            return new ResponseEntity(new ApiError("Retailer Code expressed is not found."), HttpStatus.NOT_FOUND);
        }

        // Ensure the RetailerCode is not already redeemed
        if (retailerCode.getStatus() == RetailerCode.Status.REDEEMED) {
            return new ResponseEntity(new ApiError("Retailer Code with a status of " + retailerCode.getStatus() +
                    " can't be expired."),
                    HttpStatus.BAD_REQUEST);
        }

        // Get the latest status of this retailer code from the Retailer Code Service
        // If the returned status is EXPIRED, then update the status of the Retailer Code
        try {
            ExternalRetailerCodeStatusResponse status = externalEstRetailerController.status(retailerCode, requestContext,
                    ExternalRetailerCodeStatusResponse.class);

            if (status.getStatus().equals("EXPIRED")) {
                // Update RetailerCode status to EXPIRED
                Date modifiedDate = new Date();
                retailerCode.setStatus(RetailerCode.Status.EXPIRED);
                retailerCode.setModifiedOn(modifiedDate);
                retailerCodeRepository.saveAndFlush(retailerCode);

                return new ResponseEntity<>(retailerCode, HttpStatus.OK);
            }
        } catch (HttpException e) {
            return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.CONFLICT);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(retailerCode, HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @ApiOperation(value = "Get Retailer Code information for a given code")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{code}", produces = "application/json")
    public ResponseEntity<RetailerCode> getRetailerCode(@PathVariable
                                                        @ApiParam(value = "The Retailer Code to retrieve")
                                                                String code,
                                                        @RequestHeader(value = "Request-Context", required = false)
                                                        @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                                String requestContext) {
        RetailerCode retailerCode = retailerCodeRepository.findOne(code);
        if (retailerCode == null)
            return new ResponseEntity(new ApiError("Retailer Code expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<RetailerCode>(retailerCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Retailer Codes",
            notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                    "to filter the results (AND as opposed to OR)")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400,
                    message = "Specified Content or Retailer Not Found\n" +
                            "Bad status value specified",
                    response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
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
    public ResponseEntity<Page<RetailerCode>> getRetailerCodes(
            @ApiParam(value = "Content related to Retailer Codes.")
            @RequestParam(name = "contentId", required = false)
                    Long contentId,
            @ApiParam(value = "Retailer related to Retailer Codes.")
            @RequestParam(name = "retailerId", required = false)
                    Long retailerId,
            @ApiParam(value = "Studio related to Retailer Code Content.")
            @RequestParam(name = "studioId", required = false)
                    Long studioId,
            @ApiParam(value = "Referral Partner related to Retailer Code Retailers.")
            @RequestParam(name = "partnerId", required = false)
                    Long partnerId,
            @ApiParam(value = "The format of the related Content for Retailer Codes.")
            @RequestParam(name = "format", required = false)
                    String format,
            @ApiParam(value = "Retailer Codes with this status.")
            @RequestParam(name = "status", required = false)
                    String status,
            @ApiParam(value = "Retailer Codes created after the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "createdAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date createdOnAfter,
            @ApiParam(value = "Retailer Codes created before the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "createdBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date createdOnBefore,
            @ApiParam(value = "Retailer Codes modified after the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "modifiedAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date modifiedOnAfter,
            @ApiParam(value = "Retailer Codes modified before the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "modifiedBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date modifiedOnBefore,
            @ApiParam(value = "Retailer Codes that expire after the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "expiresAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date expiresOnAfter,
            @ApiParam(value = "Retailer Codes that expire before the given date and time (yyyy-MM-dd'T'HH:mm:ss.SSSZ).")
            @RequestParam(name = "expiresBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date expiresOnBefore,
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext,
            @ApiIgnore("Ignored because swagger ui shows the wrong params, " +
                    "instead they are explained in the implicit params")
                    Pageable pageable) {

        ArrayList<SqlCriteria> params = new ArrayList<SqlCriteria>();

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("content", ":", contentId));
            }
        }

        if (retailerId != null) {
            Retailer retailer = retailerRepository.findOne(retailerId);
            if (retailer == null) {
                return new ResponseEntity(new ApiError("Retailer id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("retailer", ":", retailerId));
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

        if (partnerId != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(partnerId);
            if (referralPartner == null) {
                return new ResponseEntity(new ApiError("Referral Partner id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("retailer.partner", ":", partnerId));
            }
        }

        if (format != null) {
            params.add(new SqlCriteria("format", ":", format));
        }

        if (status != null) {
            try {
                params.add(new SqlCriteria("status", ":", RetailerCode.Status.valueOf(status)));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity(new ApiError(
                        "Status value not allowed. Please use one of: " + Arrays.asList(RetailerCode.Status.values())),
                        HttpStatus.BAD_REQUEST);
            }
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
        if (expiresOnAfter != null) {
            params.add(new SqlCriteria("expiresOn", ">", expiresOnAfter));
        }
        if (expiresOnBefore != null) {
            params.add(new SqlCriteria("expiresOn", "<", expiresOnBefore));
        }

        List<Specification<RetailerCode>> specs = new ArrayList<>();
        for (SqlCriteria param : params) {
            specs.add(new RetailerCodeSpecification(param));
        }

        Page<RetailerCode> retailerCodes;
        if (params.isEmpty()) {
            retailerCodes = retailerCodeRepository.findAll(pageable);
        } else {
            Specification<RetailerCode> query = specs.get(0);
            for (int i = 1; i < specs.size(); i++) {
                query = Specifications.where(query).and(specs.get(i));
            }
            retailerCodes = retailerCodeRepository.findAll(query, pageable);
        }

        return new ResponseEntity<Page<RetailerCode>>(retailerCodes, HttpStatus.OK);
    }

}
