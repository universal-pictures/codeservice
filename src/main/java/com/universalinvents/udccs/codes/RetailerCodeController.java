package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.pairings.PairingRepository;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import com.universalinvents.udccs.utilities.SqlCriteria;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private PairingRepository pairingRepository;

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
                                                           RetailerCode.Status.UNALLOCATED, retailer);
        return new ResponseEntity<RetailerCode>(retailerCodeRepository.save(retailerCode), HttpStatus.CREATED);
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
                                                                    String code) {
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
    public ResponseEntity<List<RetailerCode>> getRetailerCodes(
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
                    Date modifiedOnBefore) {

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

        List<Specification<RetailerCode>> specs = new ArrayList<>();
        for (SqlCriteria param : params) {
            specs.add(new RetailerCodeSpecification(param));
        }

        List<RetailerCode> retailerCodes = new ArrayList<RetailerCode>();
        if (params.isEmpty()) {
            retailerCodes = retailerCodeRepository.findAll();
        } else {
            Specification<RetailerCode> query = specs.get(0);
            for (int i = 1; i < specs.size(); i++) {
                query = Specifications.where(query).and(specs.get(i));
            }
            retailerCodes = retailerCodeRepository.findAll(query);
        }

        return new ResponseEntity<List<RetailerCode>>(retailerCodes, HttpStatus.OK);
    }

}
