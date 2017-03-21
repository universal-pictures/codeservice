package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.contents.ContentRepository;
import com.dreamworks.uddcs.exception.ApiError;
import com.dreamworks.uddcs.partners.Partner;
import com.dreamworks.uddcs.partners.PartnerRepository;
import com.dreamworks.uddcs.retailers.Retailer;
import com.dreamworks.uddcs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/codes")
public class StudioCodeController
{
    @Autowired
    private StudioCodeRepository studioCodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private RetailerCodeRepository retailerCodesRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @CrossOrigin
    @ApiOperation("Create initial Studio Code")
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity createStudioCode(@RequestBody StudioCodeRequest request)
    {
        Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        Partner partner = partnerRepository.findOne(request.getPartnerId());
        if (partner == null)
            return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.NOT_FOUND);

        String code = UUID.randomUUID().toString();
        StudioCode studioCode =
                new StudioCode(code, request.getCreatedBy(), new Date(), partner, content);
        studioCodeRepository.save(studioCode);
        return new ResponseEntity(studioCode, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Get Studio Code information for a given code")
    @RequestMapping(method= RequestMethod.GET, value = "/{code}")
    public ResponseEntity<StudioCode> getStudioCode(@PathVariable String code)
    {
        StudioCode studioCode = studioCodeRepository.findOne(code);
        if (studioCode == null)
            return new ResponseEntity(new ApiError("Studio Code expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<StudioCode>(studioCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Studio Codes")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<StudioCode>> getStudioCodes()
    {
        List<StudioCode> studioCodes = studioCodeRepository.findAll();
        return new ResponseEntity<List<StudioCode>>(studioCodes, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Pair Studio Code to a Retailer Code")
    @RequestMapping(method= RequestMethod.PUT, value = "/{code}/pair")
    public ResponseEntity<StudioCode> pairStudioCode(@PathVariable String code, @RequestBody PairStudioCodeRequest request)
    {
        StudioCode studioCode = studioCodeRepository.findOne(code);
        if (studioCode == null)
            return new ResponseEntity(new ApiError("Studio Code expressed is not found."), HttpStatus.NOT_FOUND);

        if (studioCode.isPaired())
            return new ResponseEntity(new ApiError("Studio Code expressed is already paired."), HttpStatus.CONFLICT);

        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        if (! studioCode.getContent().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("Retailer does not have requested Content."), HttpStatus.NOT_FOUND);

        if (! studioCode.getPartner().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("Partner does not have access to selected Retailer."), HttpStatus.NOT_FOUND);

        // = Check if the code in the request is in the list of codes of RetailerCodes.
        final List<RetailerCode> retailerCodes = retailerCodesRepository.findByPairedOnAndContentAndRetailer(null, studioCode.getContent(), retailer);
        final Optional<RetailerCode> retailerCode = retailerCodes.stream().filter(x->x.getCode().equalsIgnoreCase(request.getRetailerCode())).findFirst();
        if (!retailerCode.isPresent()) {
            return new ResponseEntity(new ApiError("Retailer Code expressed is not found"), HttpStatus.NOT_FOUND);
        }

        final Date pairingDate = new Date();
        studioCode.setPairedOn(pairingDate);

        // = In order to avoid providing the retailer code by URL, the code in RetailerCode entity should be Key.
        // = Right now the key for that entity is autoincrement allowing to have for 1 retailer and 1 content -> N Codes.
        studioCode.setRetailerCode(request.getRetailerCode());
        studioCode.setRetailerId(request.getRetailerId());
        studioCode.setPairedBy(request.getPairedBy());

        studioCodeRepository.save(studioCode);
        retailerCode.ifPresent(x -> {
            x.setPairedOn(pairingDate);
            retailerCodesRepository.save(x);
        });

        return new ResponseEntity<StudioCode>(studioCode, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Alter Studio Code redemption status")
    @RequestMapping(method= RequestMethod.PUT, value = "/{code}/redeem")
    public ResponseEntity<StudioCode> redeemStudioCode(@PathVariable String code, @RequestBody RedeemStudioCodeRequest request)
    {
        StudioCode studioCode = studioCodeRepository.findOne(code);
        if (studioCode == null)
            return new ResponseEntity(new ApiError("Studio Code expressed is not found."), HttpStatus.NOT_FOUND);

        if (studioCode.isRedeemed())
            return new ResponseEntity(new ApiError("Studio Code expressed is already redeemed."), HttpStatus.CONFLICT);

        studioCode.setRedeemedOn(new Date());
        studioCode.setRedeemedBy(request.getRedeemedBy());

        studioCodeRepository.save(studioCode);

        return new ResponseEntity<StudioCode>(studioCode, HttpStatus.CREATED);
    }

}
