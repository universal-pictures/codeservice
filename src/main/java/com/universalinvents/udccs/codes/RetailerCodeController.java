package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.pairings.PairingRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
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
            @RequestParam(name = "format", required = false) String format) {

        // Build a RetailerCode object with the values passed in
        RetailerCode retailerCode = new RetailerCode();

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                retailerCode.setContent(content);
            }
        }

        if (retailerId != null) {
            Retailer retailer = retailerRepository.findOne(retailerId);
            if (retailer == null) {
                return new ResponseEntity(new ApiError("Retailer id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                retailerCode.setRetailer(retailer);
            }
        }

        if (format != null) {
            retailerCode.setFormat(format);
        }

        List<RetailerCode> retailerCodes = retailerCodeRepository.findAll(Example.of(retailerCode));
        return new ResponseEntity<List<RetailerCode>>(retailerCodes, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Retailer Codes")
    @RequestMapping(method = RequestMethod.GET, value = "/test", produces = "application/json")
    public ResponseEntity<List<RetailerCode>> getRetailerCodesTest(
            @RequestParam(name = "contentId", required = false) Long contentId,
            @RequestParam(name = "retailerId", required = false) Long retailerId,
            @RequestParam(name = "format", required = false) String format,
            @RequestParam(name = "status", required = false) String status) {

        // Build a RetailerCode object with the values passed in
        RetailerCode retRetailerCode = new RetailerCode();

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                retRetailerCode.setContent(content);
            }
        }

        if (retailerId != null) {
            Retailer retailer = retailerRepository.findOne(retailerId);
            if (retailer == null) {
                return new ResponseEntity(new ApiError("Retailer id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                retRetailerCode.setRetailer(retailer);
            }
        }

        if (format != null) {
            retRetailerCode.setFormat(format);
        }

        if (status != null) {
            try {
                retRetailerCode.setStatus(RetailerCode.Status.valueOf(status));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity(new ApiError(
                        "Status value not allowed. Please use one of: " + Arrays.asList(RetailerCode.Status.values())),
                                          HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<List<RetailerCode>>(retailerCodeRepository.findAll(Example.of(retRetailerCode)),
                                                      HttpStatus.OK);
    }

}
