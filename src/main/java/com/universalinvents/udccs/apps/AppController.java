package com.universalinvents.udccs.apps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.messaging.MessagingController;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.Date;
import java.util.List;

@Api(tags = {"App Controller"},
     description = "Operations pertaining to apps")
@RestController
@RequestMapping("/api/apps")
public class AppController {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @CrossOrigin
    @ApiOperation(value = "Create an App Entry",
                  notes = "A Referral Partner can define multiple Apps that they provide to their users (i.e. iOS " +
                          "App, Android App, etc.). Most activity dealing with codes is then related to the app a " +
                          "user is using.")
    @RequestMapping(method = RequestMethod.POST,
                    produces = "application/json")
    public ResponseEntity<App> createApp(@RequestBody(required = true)
                                         @ApiParam(value = "Provide properties for a new App.")
                                                 AppRequest request) {

        ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.NOT_FOUND);

        App app = new App(); app.setDescription(request.getDescription()); app.setName(request.getName());
        app.setStatus(request.getStatus()); app.setReferralPartner(referralPartner); app.setCreatedOn(new Date());
        app.setAccessToken(request.getAccessToken());

        appRepository.save(app);

        return new ResponseEntity<App>(app, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation(value = "Update an App Entry",
                  notes = "Specify just the properties you want to change.  Any specified property will overwrite " +
                          "its existing value.")
    @RequestMapping(method = RequestMethod.PATCH,
                    value = "/{id}",
                    produces = "application/json")
    public ResponseEntity<App> updateApp(
            @PathVariable @ApiParam(value = "The id of the App you wish to change") Long id,
            @RequestBody(required = false)
            @ApiParam(value = "Provide updated properties for the App")
                    AppRequest request) {
        // Get existing App record
        App app = appRepository.findOne(id); if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false; if (request.getPartnerId() != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
            if (referralPartner == null)
                return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.NOT_FOUND);
            app.setReferralPartner(referralPartner); isModified = true;
        }

        if (request.getDescription() != null) {
            app.setDescription(request.getDescription()); isModified = true;
        } if (request.getName() != null) {
            app.setName(request.getName()); isModified = true;
        } if (request.getStatus() != null) {
            app.setStatus(request.getStatus()); isModified = true;
        }

        if (isModified) {
            app.setModifiedOn(new Date()); appRepository.save(app); return new ResponseEntity<App>(app, HttpStatus.OK);
        }

        // Nothing was updated.  Just return the found App.
        return new ResponseEntity<App>(app, HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @ApiOperation(value = "Delete an App Entry",
                  notes = "Delete an App that has not yet been associated with any codes.  If it has been associated " +
                          "with codes, it will not be deleted and will result in an error.\n\n" +
                          "After successful deletion, an SQS message is emitted on the *udccs_app_delete.fifo* queue " +
                          "in AWS to allow for additional processing if necessary.")
    @RequestMapping(method = RequestMethod.DELETE,
                    value = "/{id}",
                    produces = "application/json")
    public ResponseEntity deleteApp(@PathVariable @ApiParam(value = "The id of the App you wish to delete") Long id) {
        try {
            // First get the existing app record
            App app = appRepository.findOne(id); ObjectMapper mapper = new ObjectMapper();
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
    @ApiOperation(value = "Get App information for a given App id")
    @RequestMapping(method = RequestMethod.GET,
                    value = "/{id}",
                    produces = "application/json")
    public ResponseEntity<App> getAppById(
            @PathVariable @ApiParam(value = "The id of the App you wish to retrieve") Long id) {
        App app = appRepository.findOne(id); if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<App>(app, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Apps",
                  notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                          "to filter the results (AND as opposed to OR)")
    @RequestMapping(method = RequestMethod.GET,
                    produces = "application/json")
    public ResponseEntity<List<App>> getApps(@ApiParam(value = "Referral Partner related to Apps.")
                                             @RequestParam(name = "partnerId",
                                                           required = false) Long partnerId,
                                             @ApiParam(value = "The name of an App to find.  Exact match only.")
                                             @RequestParam(name = "name",
                                                           required = false) String name,
                                             @ApiParam(value = "An AWS access token related to an App.")
                                             @RequestParam(name = "accessToken",
                                                           required = false) String accessToken,
                                             @ApiParam(value = "Apps with the given status (ACTIVE or INACTIVE)")
                                             @RequestParam(name = "status",
                                                           required = false) String status) {

        // Build an App object with the values passed in
        App app = new App();

        if (partnerId != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(partnerId);
            if (referralPartner == null) {
                return new ResponseEntity(new ApiError("Referral Partner id specified not found."),
                                          HttpStatus.BAD_REQUEST);
            } else {
                app.setReferralPartner(referralPartner);
            }
        }

        if (name != null) {
            app.setName(name);
        }

        if (accessToken != null) {
            app.setAccessToken(accessToken);
        }

        if (status != null) {
            app.setStatus(status);
        }

        List<App> apps = appRepository.findAll(Example.of(app));
        return new ResponseEntity<List<App>>(apps, HttpStatus.OK);
    }
}
