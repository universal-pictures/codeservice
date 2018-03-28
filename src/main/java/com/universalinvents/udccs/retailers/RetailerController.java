package com.universalinvents.udccs.retailers;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Api(tags = {"Retailer Controller"},
     description = "Operations pertaining to retailers")
@RestController
@RequestMapping("/api/retailers")
public class RetailerController
{
    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private ContentRepository contentRepository;

    @CrossOrigin
    @ApiOperation(value = "Create a Retailer Entry",
                  notes = "A Retailer represents a company that provides Content to consumers and " +
                          "the ability to acquire said Content through Retailer Codes.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = Retailer.class)
    })
    @RequestMapping(method= RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Retailer> createRetailer(
            @RequestBody
            @ApiParam(value = "Provide properties for the Retailer.", required = true)
                    CreateRetailerRequest request)
    {
        Retailer retailer = new Retailer();
        retailer.setName(request.getName());
        retailer.setStatus(request.getStatus());
        retailer.setCreatedOn(new Date());

        retailerRepository.save(retailer);

        return new ResponseEntity<Retailer>(retailer, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation(value = "Update a Retailer Entry",
                  notes = "Specify values for those properties you wish to overwrite.  Please note that when " +
                          "specifying any of these values, they will overwrite existing values.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Retailer was not modified", response = Retailer.class),
            @ApiResponse(code = 404, message = "Retailer is Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = "application/json")
    public ResponseEntity<Retailer> updateRetailer(
            @PathVariable
            @ApiParam(value = "The id of the Retailer to update", required = true)
                    Long id,
            @RequestBody(required = false)
            @ApiParam(value = "Provide updated properties for the Retailer")
                    UpdateRetailerRequest request) {

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

        if (request.getStatus() != null) {
            retailer.setStatus(request.getStatus());
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
    @ApiOperation(value = "Delete a Retailer Entry",
                  notes = "Delete a Retailer record that has not yet been associated with a Retailer Code.")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deleteRetailer(@PathVariable
                                             @ApiParam(value = "The id of the Retailer to delete") Long id) {
        try {
            retailerRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Get Retailer information for a given Retailer id")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method= RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<Retailer> getRetailerById(
            @PathVariable
            @ApiParam(value = "The id of the Retailer to retrieve")
                    Long id)
    {
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Retailer>(retailer, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Retailers",
                  notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                          "to filter the results (AND as opposed to OR)")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Specified Content or Referral Partner Not Found",
                         response = ApiError.class)
    })
    @RequestMapping(method= RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Retailer>> getRetailers(
            @RequestParam(name = "contentId", required = false)
            @ApiParam(value = "Content related to Retailers.")
                    Long contentId,
            @RequestParam(name = "partnerId", required = false)
            @ApiParam(value = "Referral Partner related to Retailers.")
                    Long partnerId,
            @RequestParam(name = "name", required = false)
            @ApiParam(value = "The name of a Retailer to find.  Exact match only.")
                    String name,
            @RequestParam(name = "status", required = false)
            @ApiParam(value = "Retailers with the given status (ACTIVE or INACTIVE)")
                    String status) {

        // Build a Retailer object with the values passed in
        Retailer retailer = new Retailer();

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                retailer.setContents(Collections.singleton(content));
            }
        }

        if (partnerId != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(partnerId);
            if (referralPartner == null) {
                return new ResponseEntity(new ApiError("Referral Partner id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                retailer.setReferralPartners(Collections.singleton(referralPartner));
            }
        }

        if (name != null) {
            retailer.setName(name);
        }

        if (status != null) {
            retailer.setStatus(status);
        }

        List<Retailer> retailers = retailerRepository.findAll(Example.of(retailer));
        return new ResponseEntity<List<Retailer>>(retailers, HttpStatus.OK);
    }
}
