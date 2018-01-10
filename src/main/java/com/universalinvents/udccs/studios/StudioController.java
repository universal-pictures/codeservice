package com.universalinvents.udccs.studios;

import com.universalinvents.udccs.exception.ApiError;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/studios")
public class StudioController {
    @Autowired
    private StudioRepository studioRepository;

    @CrossOrigin
    @ApiOperation("Create a Studio Entry")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Studio> createStudio(@RequestBody StudioRequest request) {
        Studio studio = new Studio();
        studio.setName(request.getName());
        studio.setDescription(request.getDescription());
        studio.setContactName(request.getContactName());
        studio.setContactEmail(request.getContactEmail());
        studio.setContactPhone(request.getContactPhone());
        studio.setStatus(request.getStatus());
        studio.setFlags(request.getFlags());
        studio.setCodePrefix(request.getCodePrefix());
        studio.setCreatedOn(new Date());

        studioRepository.save(studio);

        return new ResponseEntity<Studio>(studio, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Update a Studio Entry")
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = "application/json")
    public ResponseEntity<Studio> updateStudio(@PathVariable Long id, @RequestBody(required = false) StudioRequest request) {
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

        if (isModified) {
            studio.setModifiedOn(new Date());
            studioRepository.save(studio);
            return new ResponseEntity<Studio>(studio, HttpStatus.OK);
        }

        // Nothing was modified.  Just return the found Studio.
        return new ResponseEntity<Studio>(studio, HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @ApiOperation("Delete a Studio Entry")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deleteStudio(@PathVariable Long id) {
        try {
            studioRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation("Get studio information for a given Studio id")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<Studio> getStudioById(@PathVariable Long id) {
        Studio studio = studioRepository.findOne(id);
        if (studio == null)
            return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Studio>(studio, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Studio List")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Studio>> getStudios() {
        List<Studio> studios = studioRepository.findAll();
        return new ResponseEntity<List<Studio>>(studios, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get studio information for a given Studio name")
    @RequestMapping(method = RequestMethod.GET, value = "/name/{name}", produces = "application/json")
    public ResponseEntity<List<Studio>> getStudioByName(@PathVariable String name) {
        List<Studio> studios = studioRepository.findByName(name);
        if (studios == null || studios.isEmpty())
            return new ResponseEntity(new ApiError("Studio name expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Studio>>(studios, HttpStatus.OK);
    }
}
