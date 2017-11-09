package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.contents.ContentRepository;
import com.dreamworks.uddcs.exception.ApiError;
import com.dreamworks.uddcs.retailers.Retailer;
import com.dreamworks.uddcs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/codes/retailer")
public class RetailerCodeController
{
    @Autowired
    private RetailerCodeRepository retailerCodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @CrossOrigin
    @ApiOperation("Create initial Retailer Code")
    @RequestMapping(method= RequestMethod.POST, produces = "application/json")
    public ResponseEntity<RetailerCode> createRetailerCode(@RequestBody @Valid RetailerCodeRequest request)
    {
        final Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        final Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        final RetailerCode retailerCode = new RetailerCode(request.getCode(), content, "NEED FORMAT", retailer);

        return new ResponseEntity<RetailerCode>(retailerCodeRepository.save(retailerCode), HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Get Retailer Code information for a given id")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<RetailerCode> getRetailerCode(@PathVariable Long id)
    {
        RetailerCode retailerCode = retailerCodeRepository.findOne(id);
        if (retailerCode == null)
            return new ResponseEntity(new ApiError("Retailer ID expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<RetailerCode>(retailerCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Retailer Codes")
    @RequestMapping(method= RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<RetailerCode>> getRetailerCodes()
    {
        List<RetailerCode> retailerCodes = retailerCodeRepository.findAll();
        return new ResponseEntity<List<RetailerCode>>(retailerCodes, HttpStatus.OK);
    }


    @CrossOrigin
    @ApiOperation("Get Retailer Codes for a given Content and Retailer")
    @RequestMapping(method= RequestMethod.GET, value = "/content/{contentId}/retailer/{retailerId}", produces = "application/json")
    public ResponseEntity<List<RetailerCode>> getRetailerCodeForRetailerAndContent(@PathVariable Long contentId, @PathVariable Long retailerId)
    {
        final Content content = contentRepository.findOne(contentId);
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        final Retailer retailer = retailerRepository.findOne(retailerId);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        final List<RetailerCode> retailerCodes = retailerCodeRepository.findByContentAndRetailer(content, retailer);

        return new ResponseEntity<List<RetailerCode>>(retailerCodes, HttpStatus.OK);
    }

}
