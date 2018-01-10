package com.universalinvents.udccs.apps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.messaging.MessagingController;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.Date;
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
        app.setCreatedOn(new Date());
        app.setAccessToken(request.getAccessToken());

        appRepository.save(app);

        return new ResponseEntity<App>(app, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Update an App Entry")
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = "application/json")
    public ResponseEntity<App> updateApp(@PathVariable Long id, @RequestBody(required = false) AppRequest request) {
        // Get existing App record
        App app = appRepository.findOne(id);
        if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false;
        if (request.getPartnerId() != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
            if (referralPartner == null)
                return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.NOT_FOUND);
            app.setReferralPartner(referralPartner);
            isModified = true;
        }

        if (request.getDescription() != null) {
            app.setDescription(request.getDescription());
            isModified = true;
        }
        if (request.getName() != null) {
            app.setName(request.getName());
            isModified = true;
        }
        if (request.getStatus() != null) {
            app.setStatus(request.getStatus());
            isModified = true;
        }

        if (isModified) {
            app.setModifiedOn(new Date());
            appRepository.save(app);
            return new ResponseEntity<App>(app, HttpStatus.OK);
        }

        // Nothing was updated.  Just return the found App.
        return new ResponseEntity<App>(app, HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @ApiOperation("Delete an App Entry")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deleteApp(@PathVariable Long id) {
        try {
            // First get the existing app record
            App app = appRepository.findOne(id);
            ObjectMapper mapper = new ObjectMapper();
            String appJson = mapper.writeValueAsString(app);

            // Now delete the record
            appRepository.delete(id);

            // Send message about the deletion
            MessagingController controller = new MessagingController();
            controller.sendMessage("udccs_app_delete.fifo", appJson);

            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.notFound().build();
        } catch (JMSException e) {
            // This situation is okay although we'll have to
            // manually clean up any Cognito records later
            return ResponseEntity.noContent().build();
        }
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
    @RequestMapping(method = RequestMethod.GET, value = "/partners/{partnerId}", produces = "application/json")
    public ResponseEntity<List<App>> getAppsByPartnerId(@PathVariable Long partnerId) {
        final ReferralPartner partner = referralPartnerRepository.findOne(partnerId);
        if (partner == null)
            return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.NOT_FOUND);

        List<App> apps = appRepository.findByReferralPartner(partner);
        return new ResponseEntity<List<App>>(apps, HttpStatus.OK);
    }
}
