package com.universalinvents.udccs.partners;

import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Api(tags = {"Referral Partner Controller"},
     description = "Operations pertaining to referral partners")
@RestController
@RequestMapping("/api/partners")
public class ReferralPartnerController {
    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private AppRepository appRepository;

    @CrossOrigin
    @ApiOperation(value = "Create a Referral Partner Entry",
                  notes = "A Referral Partner represents a company that orchestrates the purchasing and " +
                          "management of Master Codes (usually through a number of Apps they maintain).")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ReferralPartner.class),
            @ApiResponse(code = 400, message = "Specified Retailer Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ReferralPartner> createPartner(
            @RequestBody
            @ApiParam(value = "Provide properties for the Referral Partner.", required = true)
                    CreateReferralPartnerRequest request) {

        HashSet<Retailer> retailers = null;
        try {
            retailers = getRetailers(request);
        } catch (ApiError apiError) {
            return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
        }

        ReferralPartner referralPartner = new ReferralPartner();
        referralPartner.setName(request.getName());
        referralPartner.setDescription(request.getDescription());
        referralPartner.setContactName(request.getContactName());
        referralPartner.setContactEmail(request.getContactEmail());
        referralPartner.setContactPhone(request.getContactPhone());
        referralPartner.setRetailers(retailers);
        referralPartner.setStatus(request.getStatus());
        referralPartner.setCreatedOn(new Date());

        referralPartnerRepository.save(referralPartner);

        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation(value = "Update a Referral Partner Entry",
                  notes = "Specify values for those properties you wish to overwrite.  Please note that when " +
                          "specifying any of these values, they will overwrite existing values; especially " +
                          "retailerIds.  If you wish to add a Retailer to the list, you must ensure to include " +
                          "any of the current values here.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Referral Partner was not modified", response = ReferralPartner.class),
            @ApiResponse(code = 404, message = "Referral Partner is Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Specified Retailer Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = "application/json")
    public ResponseEntity<ReferralPartner> updatePartner(
            @PathVariable
            @ApiParam(value = "The id of the Referral Partner to update", required = true)
                    Long id,
            @RequestBody(required = false)
            @ApiParam(value = "Provide updated properties for the Referral Partner")
                    UpdateReferralPartnerRequest request) {

        // Get existing Partner record
        ReferralPartner referralPartner = referralPartnerRepository.findOne(id);
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("Referral Partner id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false;
        if (request.getRetailerIds() != null) {
            try {
                referralPartner.setRetailers(getRetailers(request));
                isModified = true;
            } catch (ApiError apiError) {
                return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
            }
        }

        if (request.getName() != null) {
            referralPartner.setName(request.getName());
            isModified = true;
        }
        if (request.getDescription() != null) {
            referralPartner.setDescription(request.getDescription());
            isModified = true;
        }
        if (request.getContactName() != null) {
            referralPartner.setContactName(request.getContactName());
            isModified = true;
        }
        if (request.getContactEmail() != null) {
            referralPartner.setContactEmail(request.getContactEmail());
            isModified = true;
        }
        if (request.getContactPhone() != null) {
            referralPartner.setContactPhone(request.getContactPhone());
            isModified = true;
        }
        if (request.getStatus() != null) {
            referralPartner.setStatus(request.getStatus());
            isModified = true;
        }

        if (isModified) {
            referralPartner.setModifiedOn(new Date());
            referralPartnerRepository.save(referralPartner);
            return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.OK);
        }

        // Nothing was modified.  Just return the found ReferralPartner.
        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.NOT_MODIFIED);
    }

    private HashSet<Retailer> getRetailers(ReferralPartnerRequest request) throws ApiError {
        HashSet<Retailer> retailers = new HashSet();
        for (Long retailerId : request.getRetailerIds()) {
            Retailer foundRetailer = retailerRepository.findOne(retailerId);
            if (foundRetailer == null) throw new ApiError("Retailer id " + retailerId + " is not found.");

            retailers.add(foundRetailer);
        }
        return retailers;
    }

    @CrossOrigin
    @ApiOperation(value = "Delete a Referral Partner Entry",
                  notes = "Delete a Referral Partner record that has not yet been associated with a Master Code.")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deletePartner(@PathVariable
                                            @ApiParam(value = "The id of the Referral Partner to delete")
                                                    Long id) {
        try {
            referralPartnerRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Get Referral Partner information for a given Referral Partner id")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<ReferralPartner> getPartnerById(
            @PathVariable
            @ApiParam(value = "The id of the Referral Partner to retrieve")
                    Long id) {

        ReferralPartner referralPartner = referralPartnerRepository.findOne(id);
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Referral Partners",
                  notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                          "to filter the results (AND as opposed to OR)")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Specified Retailer Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ReferralPartner>> getPartners(
            @RequestParam(name = "retailerId", required = false)
            @ApiParam(value = "Referral Partners related with this Retailer.")
                    Long retailerId,
            @RequestParam(name = "name", required = false)
            @ApiParam(value = "The name of a Referral Partner to find. Exact match only.")
                    String name,
            @RequestParam(name = "contactName", required = false)
            @ApiParam(value = "Referral Partners with this contact name. Exact match only.")
                    String contactName,
            @RequestParam(name = "contactEmail", required = false)
            @ApiParam(value = "Referral Partners with this contact email. Exact match only.")
                    String contactEmail,
            @RequestParam(name = "status", required = false)
            @ApiParam(value = "ACTIVE or INACTIVE")
                    String status) {

        // Build a ReferralPartner object with the values passed in
        ReferralPartner referralPartner = new ReferralPartner();

        if (retailerId != null) {
            Retailer retailer = retailerRepository.findOne(retailerId);
            if (retailer == null) {
                return new ResponseEntity(new ApiError("Retailer id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                referralPartner.setRetailers(Collections.singleton(retailer));
            }
        }

        if (name != null) {
            referralPartner.setName(name);
        }

        if (contactName != null) {
            referralPartner.setContactName(contactName);
        }

        if (contactEmail != null) {
            referralPartner.setContactEmail(contactEmail);
        }

        if (status != null) {
            referralPartner.setStatus(status);
        }

        List<ReferralPartner> referralPartners = referralPartnerRepository.findAll(Example.of(referralPartner));
        return new ResponseEntity<List<ReferralPartner>>(referralPartners, HttpStatus.OK);
    }
}
