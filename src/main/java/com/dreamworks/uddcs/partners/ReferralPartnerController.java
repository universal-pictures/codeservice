package com.dreamworks.uddcs.partners;

import com.dreamworks.uddcs.exception.ApiError;
import com.dreamworks.uddcs.retailers.Retailer;
import com.dreamworks.uddcs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class ReferralPartnerController
{
    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @CrossOrigin
    @ApiOperation("Create a ReferralPartner Entry")
    @RequestMapping(method= RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ReferralPartner> createPartner(@RequestBody ReferralPartnerRequest request)
    {
        ReferralPartner referralPartner = new ReferralPartner();
        referralPartner.setName(request.getName());
        referralPartner.setDescription(request.getDescription());
        referralPartner.setContactName(request.getContactName());
        referralPartner.setContactEmail(request.getEmail());
        referralPartner.setContactPhone(request.getPhone());
        referralPartner.setCreatedOn(new Date());

        referralPartnerRepository.save(referralPartner);

        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.CREATED);
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

    @CrossOrigin
    @ApiOperation("Add a Retailer for a ReferralPartner")
    @RequestMapping(method= RequestMethod.PUT, value = "/{id}", produces = "application/json")
    public ResponseEntity<ReferralPartner> addRetailerToContent(@PathVariable Long id, @RequestBody AddRetailerToReferralPartnerRequest request)
    {
        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);


        ReferralPartner referralPartner = referralPartnerRepository.findOne(id);
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);

        retailer.getReferralPartners().add(referralPartner);
        referralPartner.getRetailers().add(retailer);

        referralPartnerRepository.save(referralPartner);

        return new ResponseEntity<ReferralPartner>(referralPartner, HttpStatus.CREATED);
    }
}
