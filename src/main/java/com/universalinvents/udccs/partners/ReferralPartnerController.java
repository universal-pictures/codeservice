package com.universalinvents.udccs.partners;

import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class ReferralPartnerController
{
    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private AppRepository appRepository;

    @CrossOrigin
    @ApiOperation("Create a ReferralPartner Entry")
    @RequestMapping(method= RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ReferralPartner> createPartner(@RequestBody ReferralPartnerRequest request)
    {
        HashSet<Retailer> retailers = null;
        try {
            retailers = getRetailers(request);
        } catch (ApiError apiError) {
            return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
        }

        ReferralPartner referralPartner = new ReferralPartner();
        referralPartner.setName(request.getName());
        referralPartner.setDescription(request.getDescription());
        referralPartner.setContactName(request.getContactName());
        referralPartner.setContactEmail(request.getEmail());
        referralPartner.setContactPhone(request.getPhone());
        referralPartner.setRetailers(retailers);
        referralPartner.setCreatedOn(new Date());

        referralPartnerRepository.save(referralPartner);

        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Update a ReferralPartner Entry")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
    public ResponseEntity<ReferralPartner> updatePartner(@PathVariable Long id, @RequestBody ReferralPartnerRequest request) {
        HashSet<Retailer> retailers = null;
        try {
            retailers = getRetailers(request);
        } catch (ApiError apiError) {
            return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
        }

        // Get existing Partner record
        ReferralPartner referralPartner = referralPartnerRepository.findOne(id);
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request
        referralPartner.setName(request.getName());
        referralPartner.setDescription(request.getDescription());
        referralPartner.setContactName(request.getContactName());
        referralPartner.setContactEmail(request.getEmail());
        referralPartner.setContactPhone(request.getPhone());
        referralPartner.setRetailers(retailers);
        referralPartner.setModifiedOn(new Date());

        referralPartnerRepository.save(referralPartner);

        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.OK);
    }

    private HashSet<Retailer> getRetailers(@RequestBody ReferralPartnerRequest request) throws ApiError {
        HashSet<Retailer> retailers = new HashSet();
        for (Long retailerId : request.getRetailerIds()) {
            Retailer foundRetailer = retailerRepository.findOne(retailerId);
            if (foundRetailer == null) throw new ApiError("Retailer id " + retailerId + " is not found.");

            retailers.add(foundRetailer);
        }
        return retailers;
    }

    @CrossOrigin
    @ApiOperation("Delete a ReferralPartner Entry")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deletePartner(@PathVariable Long id) {
        try {
            referralPartnerRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation("Get ReferralPartner information for a given ReferralPartner id")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<ReferralPartner> getPartnerById(@PathVariable Long id)
    {
        ReferralPartner referralPartner = referralPartnerRepository.findOne(id);
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get ReferralPartner List")
    @RequestMapping(method= RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ReferralPartner>> getPartners()
    {
        List<ReferralPartner> referralPartners = referralPartnerRepository.findAll();
        return new ResponseEntity<List<ReferralPartner>>(referralPartners, HttpStatus.OK);
    }

//    @CrossOrigin
//    @ApiOperation("Add a Retailer for a ReferralPartner")
//    @RequestMapping(method= RequestMethod.PUT, value = "/{id}", produces = "application/json")
//    public ResponseEntity<ReferralPartner> addRetailerToContent(@PathVariable Long id, @RequestBody AddRetailerToReferralPartnerRequest request)
//    {
//        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
//        if (retailer == null)
//            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);
//
//
//        ReferralPartner referralPartner = referralPartnerRepository.findOne(id);
//        if (referralPartner == null)
//            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);
//
//        retailer.getReferralPartners().add(referralPartner);
//        referralPartner.getRetailers().add(retailer);
//
//        referralPartnerRepository.save(referralPartner);
//
//        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.CREATED);
//    }

    @CrossOrigin
    @ApiOperation("Get all Apps for the given ReferralPartner id")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}/apps")
    public ResponseEntity<List<App>> getAppsByPartnerId(@PathVariable Long id)
    {
        ReferralPartner referralPartner = referralPartnerRepository.findOne(id);
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);

        List<App> apps = appRepository.findByReferralPartner(referralPartner);
        return new ResponseEntity<List<App>>(apps, HttpStatus.OK);
    }
}
