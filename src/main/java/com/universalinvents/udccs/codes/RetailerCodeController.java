package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.pairings.PairingRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
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
    private PairingRepository pairingRepository;

    @CrossOrigin
    @ApiOperation("Import a Retailer Code")
    @RequestMapping(method = RequestMethod.POST, value = "/{code}", produces = "application/json")
    public ResponseEntity<RetailerCode> importRetailerCode(@PathVariable String code,
                                                           @RequestBody @Valid RetailerCodeRequest request) {
        final Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        final Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        final RetailerCode retailerCode = new RetailerCode(code, content, request.getFormat(),
                                                           RetailerCode.Status.UNALLOCATED, retailer);
        return new ResponseEntity<RetailerCode>(retailerCodeRepository.save(retailerCode), HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Get Retailer Code information for a given code")
    @RequestMapping(method = RequestMethod.GET, value = "/{code}", produces = "application/json")
    public ResponseEntity<RetailerCode> getRetailerCode(@PathVariable String code) {
        RetailerCode retailerCode = retailerCodeRepository.findOne(code);
        if (retailerCode == null)
            return new ResponseEntity(new ApiError("Retailer Code expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<RetailerCode>(retailerCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Retailer Codes")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<RetailerCode>> getRetailerCodes(
            @RequestParam(name = "contentId", required = false) Long contentId,
            @RequestParam(name = "retailerId", required = false) Long retailerId,
            @RequestParam(name = "format", required = false) String format,
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

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new CodeCriteria("content", ":", contentId));
            }
        }

        if (retailerId != null) {
            Retailer retailer = retailerRepository.findOne(retailerId);
            if (retailer == null) {
                return new ResponseEntity(new ApiError("Retailer id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new CodeCriteria("retailer", ":", retailerId));
            }
        }

        if (format != null) {
            params.add(new CodeCriteria("format", ":", format));
        }

        if (status != null) {
            try {
                params.add(new CodeCriteria("status", ":", RetailerCode.Status.valueOf(status)));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity(new ApiError(
                        "Status value not allowed. Please use one of: " + Arrays.asList(RetailerCode.Status.values())),
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

        List<Specification<RetailerCode>> specs = new ArrayList<>();
        for (CodeCriteria param : params) {
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
