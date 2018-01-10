package com.universalinvents.udccs.retailers;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.partners.ReferralPartner;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/retailers")
public class RetailerController
{
    @Autowired
    private RetailerRepository retailerRepository;

    @CrossOrigin
    @ApiOperation("Create a Retailer Entry")
    @RequestMapping(method= RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Retailer> createRetailer(@RequestBody RetailerRequest request)
    {
        Retailer retailer = new Retailer();
        retailer.setName(request.getName());
        retailer.setRegionCode(request.getRegionCode());
        retailer.setCreatedOn(new Date());

        retailerRepository.save(retailer);

        return new ResponseEntity<Retailer>(retailer, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Update a Retailer Entry")
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = "application/json")
    public ResponseEntity<Retailer> updateRetailer(@PathVariable Long id, @RequestBody(required = false) RetailerRequest request) {
        // Get existing Retailer record
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false;
        if (request.getName() != null) {
            retailer.setName(request.getName());
            isModified = true;
        }
        if (request.getRegionCode() != null) {
            retailer.setRegionCode(request.getRegionCode());
            isModified = true;
        }

        if (isModified) {
            retailer.setModifiedOn(new Date());
            retailerRepository.save(retailer);
            return new ResponseEntity<Retailer>(retailer, HttpStatus.OK);
        }

        // Nothing was modified.  Just return the found Retailer.
        return new ResponseEntity<Retailer>(retailer, HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @ApiOperation("Delete a Retailer Entry")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deleteRetailer(@PathVariable Long id) {
        try {
            retailerRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation("Get Retailer information for a given Retailer id")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<Retailer> getRetailerById(@PathVariable Long id)
    {
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Retailer>(retailer, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Contents for a given Retailer")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}/contents", produces = "application/json")
    public ResponseEntity<Set<Content>> getContentsForRetailId(@PathVariable Long id)
    {
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Set<Content>>(retailer.getContents(), HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Partners for a given Retailer")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}/partners", produces = "application/json")
    public ResponseEntity<Set<ReferralPartner>> getPartnersForRetailId(@PathVariable Long id)
    {
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Set<ReferralPartner>>(retailer.getReferralPartners(), HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Retailer List")
    @RequestMapping(method= RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Retailer>> getRetailers()
    {
        List<Retailer> retailers = retailerRepository.findAll();
        return new ResponseEntity<List<Retailer>>(retailers, HttpStatus.OK);
    }
}
