/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.universalinvents.codeservice.apps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.universalinvents.codeservice.exception.ApiError;
import com.universalinvents.codeservice.messaging.MessagingController;
import com.universalinvents.codeservice.partners.ReferralPartner;
import com.universalinvents.codeservice.partners.ReferralPartnerRepository;
import com.universalinvents.codeservice.utilities.ApiDefinitions;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.jms.JMSException;
import java.util.Date;

@Api(tags = {"App Controller"},
        description = "Operations pertaining to apps")
@RestController
@RequestMapping("/api/apps")
public class AppController {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private MessagingController messagingController;

    @CrossOrigin
    @ApiOperation(value = "Create an App Entry",
            notes = "A Referral Partner can define multiple Apps that they provide to their users (i.e. iOS " +
                    "App, Android App, etc.). Most activity dealing with codes is then related to the app a " +
                    "user is using.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = App.class),
            @ApiResponse(code = 400, message = "Specified Referral Partner Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<App> createApp(@RequestBody
                                         @ApiParam(value = "Provide properties for a new App.", required = true)
                                                 AppRequest request,
                                         @RequestHeader(value = "Request-Context", required = false)
                                         @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                 String requestContext) {

        ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
        if (referralPartner == null)
            return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.BAD_REQUEST);

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
    @ApiOperation(value = "Update an App Entry",
            notes = "Specify just the properties you want to change.  Any specified property will overwrite " +
                    "its existing value.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "App was not modified", response = App.class),
            @ApiResponse(code = 404, message = "App is Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Specified Referral Partner Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PATCH,
            value = "/{id}",
            produces = "application/json")
    public ResponseEntity<App> updateApp(
            @PathVariable
            @ApiParam(value = "The id of the App to change")
                    Long id,
            @RequestBody(required = false)
            @ApiParam(value = "Provide updated properties for the App")
                    AppRequest request,
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        // Get existing App record
        App app = appRepository.findOne(id);
        if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false;
        if (request.getPartnerId() != null) {
            ReferralPartner referralPartner = referralPartnerRepository.findOne(request.getPartnerId());
            if (referralPartner == null)
                return new ResponseEntity(new ApiError("Partner id expressed is not found."), HttpStatus.BAD_REQUEST);
            app.setReferralPartner(referralPartner);
            isModified = true;
        }

        if (request.getDescription() != null) {
            app.setDescription(request.getDescription());
            isModified = true;
        }

        if (request.getAccessToken() != null) {
            app.setAccessToken(request.getAccessToken());
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
    @ApiOperation(value = "Delete an App Entry",
            notes = "Delete an App that has not yet been associated with any codes.  If it has been associated " +
                    "with codes, it will not be deleted and will result in an error.\n\n" +
                    "After successful deletion, an SQS message is emitted on the *udccs_app_delete.fifo* queue " +
                    "in AWS to allow for additional processing if necessary.")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
            @ApiResponse(code = 500, message = "Data integrity violation", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{id}",
            produces = "application/json")
    public ResponseEntity deleteApp(@PathVariable @ApiParam(value = "The id of the App to delete") Long id,
                                    @RequestHeader(value = "Request-Context", required = false)
                                    @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                            String requestContext) {
        try {
            // First get the existing app record
            App app = appRepository.findOne(id);
            ObjectMapper mapper = new ObjectMapper();
            String appJson = mapper.writeValueAsString(app);

            // Now delete the record
            appRepository.delete(id);

            // Send message about the deletion
            messagingController.sendMessage("udccs_app_delete.fifo", appJson);

            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException | JsonProcessingException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            Throwable cause = ((DataIntegrityViolationException) e).getRootCause();
            if (cause instanceof MySQLIntegrityConstraintViolationException) {
                return new ResponseEntity<>(new ApiError("Unable to delete because of dependencies"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiError("Unable to delete because of a data integrity violation"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JMSException e) {
            // This situation is okay although we'll have to
            // manually clean up any Cognito records later
            return ResponseEntity.noContent().build();
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Get App information for a given App id")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/{id}",
            produces = "application/json")
    public ResponseEntity<App> getAppById(
            @PathVariable @ApiParam(value = "The id of the App to retrieve") Long id,
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        App app = appRepository.findOne(id);
        if (app == null)
            return new ResponseEntity(new ApiError("App id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<App>(app, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Apps",
            notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                    "to filter the results (AND as opposed to OR)")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Specified Referral Partner Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    public ResponseEntity<Page<App>> getApps(@ApiParam(value = "Referral Partner related to Apps.")
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
                                                     required = false) String status,
                                             @RequestHeader(value = "Request-Context", required = false)
                                             @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                     String requestContext,
                                             @ApiIgnore("Ignored because swagger ui shows the wrong params, " +
                                                     "instead they are explained in the implicit params")
                                                     Pageable pageable) {

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

        Page<App> apps = appRepository.findAll(Example.of(app), pageable);
        return new ResponseEntity<Page<App>>(apps, HttpStatus.OK);
    }
}
