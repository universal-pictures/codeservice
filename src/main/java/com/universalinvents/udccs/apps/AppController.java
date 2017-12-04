package com.universalinvents.udccs.apps;

import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apps")
public class AppController {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @CrossOrigin
    @ApiOperation("Create an App Entry")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<App> createApp(@RequestBody AppRequest request) {

        ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.NOT_FOUND);

        App app = new App();
        app.setDescription(request.getDescription());
        app.setName(request.getName());
        app.setStatus(request.getStatus());
        app.setReferralPartner(referralPartner);

        appRepository.save(app);

        return new ResponseEntity<App>(app, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Get app information for a given App id")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<App> getAppById(@PathVariable Long id) {
        App app = appRepository.findOne(id);
        if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<App>(app, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get App List")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<App>> getApps() {
        List<App> apps = appRepository.findAll();
        return new ResponseEntity<List<App>>(apps, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Apps for a given Referral Partner id")
    @RequestMapping(method = RequestMethod.GET, value = "/partner/{partnerId}", produces = "application/json")
    public ResponseEntity<List<App>> getAppsByPartnerId(@PathVariable Long partnerId) {
        final ReferralPartner partner = referralPartnerRepository.findOne(partnerId);
        if (partner == null)
            return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.NOT_FOUND);

        List<App> apps = appRepository.findByReferralPartner(partner);
        return new ResponseEntity<List<App>>(apps, HttpStatus.OK);
    }
}
