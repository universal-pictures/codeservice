package com.dreamworks.uddcs.partners;

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
public class PartnerController
{
    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @CrossOrigin
    @ApiOperation("Create a Partner Entry")
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Partner> createPartner(@RequestBody PartnerRequest request)
    {
        Partner partner = new Partner();
        partner.setName(request.getName());
        partner.setDescription(request.getDescription());
        partner.setContactName(request.getContactName());
        partner.setEmail(request.getEmail());
        partner.setPhone(request.getPhone());
        partner.setCreatedOn(new Date());

        partnerRepository.save(partner);

        return new ResponseEntity<Partner>(partner, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Get Partner information for a given Partner id")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Partner> getPartnerById(@PathVariable String id)
    {
        Partner partner = partnerRepository.findOne(id);
        if (partner == null)
            return new ResponseEntity("Content id expressed is not found.", HttpStatus.NOT_FOUND);

        return new ResponseEntity<Partner>(partner, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Partner List")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<Partner>> getPartners()
    {
        List<Partner> partners = partnerRepository.findAll();
        return new ResponseEntity<List<Partner>>(partners, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Add a Retailer for a Partner")
    @RequestMapping(method= RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Partner> addRetailerToContent(@PathVariable String id, @RequestBody AddRetailerToPartnerRequest request)
    {
        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity("Retailer id expressed is not found.", HttpStatus.NOT_FOUND);


        Partner partner = partnerRepository.findOne(id);
        if (partner == null)
            return new ResponseEntity("Partner id expressed is not found.", HttpStatus.NOT_FOUND);

        retailer.getPartners().add(partner);
        partner.getRetailers().add(retailer);

        partnerRepository.save(partner);

        return new ResponseEntity<Partner>(partner, HttpStatus.CREATED);
    }
}
