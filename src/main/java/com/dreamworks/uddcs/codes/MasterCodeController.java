package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.apps.App;
import com.dreamworks.uddcs.apps.AppRepository;
import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.contents.ContentRepository;
import com.dreamworks.uddcs.exception.ApiError;
import com.dreamworks.uddcs.pairings.PairMasterCodeRequest;
import com.dreamworks.uddcs.pairings.Pairing;
import com.dreamworks.uddcs.pairings.PairingPK;
import com.dreamworks.uddcs.pairings.PairingRepository;
import com.dreamworks.uddcs.partners.ReferralPartner;
import com.dreamworks.uddcs.partners.ReferralPartnerRepository;
import com.dreamworks.uddcs.retailers.Retailer;
import com.dreamworks.uddcs.retailers.RetailerRepository;
import com.dreamworks.uddcs.studios.Studio;
import com.dreamworks.uddcs.studios.StudioRepository;
import com.dreamworks.uddcs.utilities.CCFUtility;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/codes/master")
public class MasterCodeController {
    @Autowired
    private MasterCodeRepository masterCodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private RetailerCodeRepository retailerCodesRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private PairingRepository pairingRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private StudioRepository studioRepository;

    @CrossOrigin
    @ApiOperation("Create initial Master Code")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<MasterCode> createMasterCode(@RequestBody MasterCodeRequest request) {
        Content content = contentRepository.findOne(request.getContentId());
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("ReferralPartner id expressed is not found."), HttpStatus.NOT_FOUND);

        Studio studio = studioRepository.findOne(request.getStudioId());
        if (studio == null)
            return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);

        String code = CCFUtility.generateCode(studio.getCodePrefix());

        MasterCode masterCode = new MasterCode(code, request.getCreatedBy(), new Date(), referralPartner, content);
        masterCodeRepository.save(masterCode);
        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Get Master Code information for a given code")
    @RequestMapping(method = RequestMethod.GET, value = "/{code}", produces = "application/json")
    public ResponseEntity<MasterCode> getMasterCode(@PathVariable String code) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Master Codes")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<MasterCode>> getMasterCodes() {
        List<MasterCode> masterCodes = masterCodeRepository.findAll();
        return new ResponseEntity<List<MasterCode>>(masterCodes, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Master Codes for a given App id")
    @RequestMapping(method = RequestMethod.GET, value = "/app/{appId}")
    public ResponseEntity<List<MasterCode>> getMasterCodesByAppId(@PathVariable Long appId) {
        App app = appRepository.findOne(appId);
        if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        List<MasterCode> masterCodes = masterCodeRepository.findByApp(app);
        return new ResponseEntity<List<MasterCode>>(masterCodes, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Pair Master Code to a Retailer Code")
    @RequestMapping(method = RequestMethod.PUT, value = "/{code}/pair", produces = "application/json")
    public ResponseEntity<MasterCode> pairMasterCode(@PathVariable String code,
                                                     @RequestBody PairMasterCodeRequest request) {
        MasterCode masterCode = masterCodeRepository.findOne(code);
        if (masterCode == null)
            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);

        if (masterCode.isPaired())
            return new ResponseEntity(new ApiError("Master Code expressed is already paired."), HttpStatus.CONFLICT);

        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);

        if (!masterCode.getContent().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("Retailer does not have requested Content."), HttpStatus.NOT_FOUND);

        if (!masterCode.getReferralPartner().getRetailers().contains(retailer))
            return new ResponseEntity(new ApiError("ReferralPartner does not have access to selected Retailer."),
                                      HttpStatus.NOT_FOUND);

        // = Check if the code in the request is in the list of codes of RetailerCodes.
        final RetailerCode retailerCode = retailerCodesRepository.findFirstByContentAndRetailer(masterCode.getContent(),
                                                                                                           retailer);
        if (retailerCode == null) {
            return new ResponseEntity(new ApiError("Retailer Code not available for selected Content"),
                                      HttpStatus.NOT_FOUND);
        }

        // Insert new pairings record
        Pairing pairing = new Pairing(new PairingPK(masterCode.getCode(), retailerCode.getCode()),
                                      request.getPairedBy(), request.getStatus());
        pairingRepository.save(pairing);

        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
    }

//    @CrossOrigin
//    @ApiOperation("Alter Master Code redemption status")
//    @RequestMapping(method = RequestMethod.PUT, value = "/{code}/redeem")
//    public ResponseEntity<MasterCode> redeemMasterCode(@PathVariable String code,
//                                                       @RequestBody RedeemMasterCodeRequest request) {
//        MasterCode masterCode = masterCodeRepository.findOne(code);
//        if (masterCode == null)
//            return new ResponseEntity(new ApiError("Master Code expressed is not found."), HttpStatus.NOT_FOUND);
//
//        if (masterCode.isRedeemed())
//            return new ResponseEntity(new ApiError("Master Code expressed is already redeemed."), HttpStatus.CONFLICT);
//
//        masterCode.setRedeemedOn(new Date());
//        masterCode.setRedeemedBy(request.getRedeemedBy());
//
//        masterCodeRepository.save(masterCode);
//
//        return new ResponseEntity<MasterCode>(masterCode, HttpStatus.CREATED);
//    }

}
