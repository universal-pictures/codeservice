package com.universalinvents.udccs.studios;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.utilities.ApiDefinitions;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;

@Api(tags = {"Studio Controller"},
     description = "Operations pertaining to studios")
@RestController
@RequestMapping("/api/studios")
public class StudioController {
    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ContentRepository contentRepository;

    @CrossOrigin
    @ApiOperation(value = "Create a Studio Entry",
                  notes = "A Studio represents a company that creates Content.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = Studio.class)
    })
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Studio> createStudio(
            @RequestBody
            @ApiParam(value = "Provide properties for the Studio.", required = true)
                    CreateStudioRequest request,
            @RequestHeader(value="Request-Context", required=false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        Studio studio = new Studio();
        studio.setName(request.getName());
        studio.setDescription(request.getDescription());
        studio.setContactName(request.getContactName());
        studio.setContactEmail(request.getContactEmail());
        studio.setContactPhone(request.getContactPhone());
        studio.setStatus(request.getStatus());
        studio.setFlags(request.getFlags());
        studio.setCodePrefix(request.getCodePrefix());
        studio.setLogoUrl(request.getLogoUrl());
        studio.setCreatedOn(new Date());
        studio.setExternalId(request.getExternalId());

        studioRepository.save(studio);

        return new ResponseEntity<Studio>(studio, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation(value = "Update a Studio Entry",
                  notes = "Specify just the properties you want to change.  Any specified property will overwrite " +
                          "its existing value.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Studio was not modified", response = Studio.class),
            @ApiResponse(code = 404, message = "Studio is Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = "application/json")
    public ResponseEntity<Studio> updateStudio(
            @PathVariable
            @ApiParam(value = "The id of the Studio to change")
                    Long id,
            @RequestBody(required = false)
            @ApiParam(value = "Provide updated properties for the Studio")
                    UpdateStudioRequest request,
            @RequestHeader(value="Request-Context", required=false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        // Get existing Studio record
        Studio studio = studioRepository.findOne(id);
        if (studio == null)
            return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false;
        if (request.getName() != null) {
            studio.setName(request.getName());
            isModified = true;
        }
        if (request.getDescription() != null) {
            studio.setDescription(request.getDescription());
            isModified = true;
        }
        if (request.getContactName() != null) {
            studio.setContactName(request.getContactName());
            isModified = true;
        }
        if (request.getContactEmail() != null) {
            studio.setContactEmail(request.getContactEmail());
            isModified = true;
        }
        if (request.getContactPhone() != null) {
            studio.setContactPhone(request.getContactPhone());
            isModified = true;
        }
        if (request.getStatus() != null) {
            studio.setStatus(request.getStatus());
            isModified = true;
        }
        if (request.getFlags() != null) {
            studio.setFlags(request.getFlags());
            isModified = true;
        }
        if (request.getCodePrefix() != null) {
            studio.setCodePrefix(request.getCodePrefix());
            isModified = true;
        }
        if (request.getLogoUrl() != null) {
            studio.setLogoUrl(request.getLogoUrl());
            isModified = true;
        }
        if (request.getExternalId() != null) {
            studio.setExternalId(request.getExternalId());
            isModified = true;
        }

        if (isModified) {
            studio.setModifiedOn(new Date());
            studioRepository.save(studio);
            return new ResponseEntity<Studio>(studio, HttpStatus.OK);
        }

        // Nothing was modified.  Just return the found Studio.
        return new ResponseEntity<Studio>(studio, HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @ApiOperation(value = "Delete a Studio Entry")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deleteStudio(@PathVariable @ApiParam(value = "The id of the Studio to delete") Long id,
                                       @RequestHeader(value="Request-Context", required=false)
                                       @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                               String requestContext) {
        try {
            studioRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Get Studio information for a given Studio id")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<Studio> getStudioById(
            @PathVariable @ApiParam(value = "The id of the Studio to retrieve") Long id,
            @RequestHeader(value="Request-Context", required=false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        Studio studio = studioRepository.findOne(id);
        if (studio == null)
            return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Studio>(studio, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Studios",
                  notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                          "to filter the results (AND as opposed to OR)")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Specified Content Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page<Studio>> getStudios(
            @RequestParam(name = "contentId", required = false)
            @ApiParam(value = "Content related to Studios.")
                    Long contentId,
            @RequestParam(name = "name", required = false)
            @ApiParam(value = "The name of a Studio to find.  Exact match only.")
                    String name,
            @RequestParam(name = "contactName", required = false)
            @ApiParam(value = "Studios with this contact name. Exact match only.")
                    String contactName,
            @RequestParam(name = "contactEmail", required = false)
            @ApiParam(value = "Studios with this contact email. Exact match only.")
                    String contactEmail,
            @RequestParam(name = "codePrefix", required = false)
            @ApiParam(value = "Studios with this code prefix value. Exact match only.")
                    String codePrefix,
            @RequestParam(name = "flags", required = false)
            @ApiParam(value = "Studios with this flag value. Exact match only.")
                    Long flags,
            @RequestParam(name = "status", required = false)
            @ApiParam(value = "ACTIVE or INACTIVE")
                    String status,
            @RequestParam(name = "externalId", required = false)
            @ApiParam(value = "Studios with this external id.")
                    String externalId,
            @RequestHeader(value="Request-Context", required=false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext,
            Pageable pageable) {

        // Build a Studio object with the values passed in
        Studio studio = new Studio();

        if (contentId != null) {
            Content content = contentRepository.findOne(contentId);
            if (content == null) {
                return new ResponseEntity(new ApiError("Content id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                studio.setContents(Collections.singletonList(content));
            }
        }

        if (name != null) {
            studio.setName(name);
        }

        if (contactName != null) {
            studio.setContactName(contactName);
        }

        if (contactEmail != null) {
            studio.setContactEmail(contactEmail);
        }
        if (codePrefix != null) {
            studio.setCodePrefix(codePrefix);
        }
        if (flags != null) {
            studio.setFlags(flags);
        }

        if (status != null) {
            studio.setStatus(status);
        }

        if (externalId != null) {
            studio.setExternalId(externalId);
        }

        Page<Studio> studios = studioRepository.findAll(Example.of(studio), pageable);
        return new ResponseEntity<Page<Studio>>(studios, HttpStatus.OK);
    }
}
