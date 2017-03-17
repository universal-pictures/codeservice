package com.dreamworks.uddcs.retailers;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.exception.ApiError;
import com.dreamworks.uddcs.partners.Partner;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiOperation("Create a Retail Entry")
    @RequestMapping(method= RequestMethod.POST)
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
    @ApiOperation("Get Retailer information for a given Retailer id")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Retailer> getRetailerById(@PathVariable Long id)
    {
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Retailer>(retailer, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Contents for a given Retailer")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}/contents")
    public ResponseEntity<Set<Content>> getContentsForRetailId(@PathVariable Long id)
    {
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Set<Content>>(retailer.getContents(), HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Partners for a given Retailer")
    @RequestMapping(method= RequestMethod.GET, value = "/{id}/partners")
    public ResponseEntity<Set<Partner>> getPartnersForRetailId(@PathVariable Long id)
    {
        Retailer retailer = retailerRepository.findOne(id);
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Set<Partner>>(retailer.getPartners(), HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Retailer List")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<Retailer>> getRetailers()
    {
        List<Retailer> retailers = retailerRepository.findAll();
        return new ResponseEntity<List<Retailer>>(retailers, HttpStatus.OK);
    }
}
