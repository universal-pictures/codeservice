package com.universalinvents.udccs.contents;

import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {
    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private StudioRepository studioRepository;

    @CrossOrigin
    @ApiOperation("Create a Content Entry")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Content> createContent(@RequestBody ContentRequest request) {

        HashSet<Retailer> retailers = null;
        try {
            retailers = getRetailers(request);
        } catch (ApiError apiError) {
            return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
        }

        Studio studio = studioRepository.findOne(request.getStudioId());
        if (studio == null)
            return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);

        Content content = new Content();
        content.setTitle(request.getTitle());
        content.setEidr(request.getEidr());
        content.setEidrv(request.getEidrv());
        content.setGtm(request.getGtm());
        content.setStatus(request.getStatus());
        content.setMsrp(request.getMsrp());
        content.setStudio(studio);
        content.setRetailers(retailers);
        content.setCreatedOn(new Date());

        contentRepository.save(content);

        return new ResponseEntity<Content>(content, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Update a Content Entry")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
    public ResponseEntity<Content> updateContent(@PathVariable Long id, @RequestBody(required = false) ContentRequest request) {

        // Get existing Content record
        Content content = contentRepository.findOne(id);
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false;
        if (request.getRetailerIds() != null) {
            try {
                content.setRetailers(getRetailers(request));
                isModified = true;
            } catch (ApiError apiError) {
                return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
            }
        }

        if (request.getStudioId() != null) {
            Studio studio = studioRepository.findOne(request.getStudioId());
            if (studio == null) {
                return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.NOT_FOUND);
            }
            content.setStudio(studio);
            isModified = true;
        }

        if (request.getTitle() != null) {
            content.setTitle(request.getTitle());
            isModified = true;
        }
        if (request.getEidr() != null) {
            content.setEidr(request.getEidr());
            isModified = true;
        }
        if (request.getEidrv() != null) {
            content.setEidrv(request.getEidrv());
            isModified = true;
        }
        if (request.getGtm() != null) {
            content.setGtm(request.getGtm());
            isModified = true;
        }
        if (request.getStatus() != null) {
            content.setStatus(request.getStatus());
            isModified = true;
        }
        if (request.getMsrp() != null) {
            content.setMsrp(request.getMsrp());
            isModified = true;
        }

        if (isModified) {
            content.setModifiedOn(new Date());
            contentRepository.save(content);
            return new ResponseEntity<Content>(content, HttpStatus.OK);
        }

        // Nothing was modified.  Just return the found Content.
        return new ResponseEntity<Content>(content, HttpStatus.NOT_MODIFIED);
    }

    private HashSet<Retailer> getRetailers(ContentRequest request) throws ApiError {
        HashSet<Retailer> retailers = new HashSet();
        for (Long retailerId : request.getRetailerIds()) {
            Retailer foundRetailer = retailerRepository.findOne(retailerId);
            if (foundRetailer == null) throw new ApiError("Retailer id " + retailerId + " is not found.");

            retailers.add(foundRetailer);
        }
        return retailers;
    }

    @CrossOrigin
    @ApiOperation("Delete a Content Entry")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deleteContent(@PathVariable Long id) {
        try {
            contentRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation("Get Content information for a given Content id")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        Content content = contentRepository.findOne(id);
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Content>(content, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Content List")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Content>> getContents() {
        List<Content> contents = contentRepository.findAll();
        return new ResponseEntity<List<Content>>(contents, HttpStatus.OK);
    }

//    @CrossOrigin
//    @ApiOperation("Add a Retailer for Content")
//    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
//    public ResponseEntity<Content> addRetailerToContent(@PathVariable Long id,
//                                                        @RequestBody AddRetailerToContentRequest request) {
//        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
//        if (retailer == null)
//            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);
//
//        Content content = contentRepository.findOne(id);
//        if (content == null)
//            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);
//
//        retailer.getContents().add(content);
//        content.getRetailers().add(retailer);
//
//        contentRepository.save(content);
//
//        return new ResponseEntity<Content>(content, HttpStatus.CREATED);
//    }
}
