package com.universalinvents.udccs.contents;

import com.universalinvents.udccs.exception.ApiError;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import com.universalinvents.udccs.utilities.ApiDefinitions;
import com.universalinvents.udccs.utilities.SqlCriteria;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = {"Content Controller"},
     description = "Operations pertaining to contents (movie titles)")
@RestController
@RequestMapping("/api/contents")
public class ContentController {
    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private StudioRepository studioRepository;

    @CrossOrigin
    @ApiOperation(value = "Create a Content Entry",
                  notes = "A Content record represents a basic movie title with minimal metadata. At this moment " +
                  "an important property is *eidr* since it is utilized to look up additional metadata " +
                  "from external movie title services.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = Content.class),
            @ApiResponse(code = 400, message = "Specified Studio Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Content> createContent(@RequestBody
                                                     @ApiParam(value = "Provide properties for the Content.",
                                                               required = true)
                                                         CreateContentRequest request,
                                                 @RequestHeader(value="Request-Context", required=false)
                                                     @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                             String requestContext) {

        Studio studio = studioRepository.findOne(request.getStudioId());
        if (studio == null)
            return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.BAD_REQUEST);

        Content content = new Content();
        content.setTitle(request.getTitle());
        content.setEidr(request.getEidr());
        content.setEidrv(request.getEidrv());
        content.setGtm(request.getGtm());
        content.setStatus(request.getStatus());
        content.setMsrp(request.getMsrp());
        content.setStudio(studio);
        content.setCreatedOn(new Date());

        contentRepository.save(content);

        return new ResponseEntity<Content>(content, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation(value = "Update a Content Entry",
                  notes = "You may update any properties of a Content record except its EIDR.  Specify values " +
                  "for those properties you wish to overwrite.")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Content was not modified", response = Content.class),
            @ApiResponse(code = 404, message = "Content is Not Found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Specified Studio Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = "application/json")
    public ResponseEntity<Content> updateContent(@PathVariable
                                                     @ApiParam(value = "The Content id to update", required = true)
                                                             Long id,
                                                 @RequestBody(required = false)
                                                 @ApiParam(value = "Provide updated properties for the Content")
                                                         UpdateContentRequest request,
                                                 @RequestHeader(value="Request-Context", required=false)
                                                     @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                             String requestContext) {

        // Get existing Content record
        Content content = contentRepository.findOne(id);
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        // Update values from request - if set
        boolean isModified = false;

        if (request.getStudioId() != null) {
            Studio studio = studioRepository.findOne(request.getStudioId());
            if (studio == null) {
                return new ResponseEntity(new ApiError("Studio id expressed is not found."), HttpStatus.BAD_REQUEST);
            }
            content.setStudio(studio);
            isModified = true;
        }

        if (request.getTitle() != null) {
            content.setTitle(request.getTitle());
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

    @CrossOrigin
    @ApiOperation(value = "Delete a Content Entry",
                  notes = "Delete a Content record that has not yet been associated with a Master Code or " +
                  "Retailer Code.")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity deleteContent(@PathVariable
                                            @ApiParam(value = "The id of the Content to delete")
                                                    Long id,
                                        @RequestHeader(value="Request-Context", required=false)
                                            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                    String requestContext) {
        try {
            contentRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Get Content information for a given Content id")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<Content> getContentById(@PathVariable
                                                      @ApiParam(value = "The id of the Content to retrieve")
                                                              Long id,
                                                  @RequestHeader(value="Request-Context", required=false)
                                                      @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                                                              String requestContext) {
        Content content = contentRepository.findOne(id);
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Content>(content, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Search Content",
                  notes = "All parameters are optional.  If multiple parameters are specified, all are used together " +
                          "to filter the results (AND as opposed to OR)")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Specified Retailer or Studio Not Found", response = ApiError.class)
    })
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page<Content>> getContents(
            @RequestParam(name = "eidr", required = false)
            @ApiParam(value = "A unique EIDR value")
                    String eidr,
            @RequestParam(name = "eidrv", required = false)
            @ApiParam(value = "An EIDRV (version)")
                    String eidrv,
            @RequestParam(name = "gtm", required = false)
            @ApiParam(value = "A GTM value (Universal Pictures id)")
                    String gtm,
            @RequestParam(name = "title", required = false)
            @ApiParam(value = "The title of a movie to find. Exact match only.")
                    String title,
            @RequestParam(name = "studioId", required = false)
            @ApiParam(value = "Studio related to Content")
                    Long studioId,
            @RequestParam(name = "status", required = false)
            @ApiParam(value = "Content with the given status (ACTIVE or INACTIVE)")
                    String status,
            @RequestParam(name = "createdAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @ApiParam(value = "Content created after the given date and time (yyyy-MM-dd’T’HH:mm:ss.SSSZ)")
                    Date createdOnAfter,
            @RequestParam(name = "createdBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @ApiParam(value = "Content created before the given date and time (yyyy-MM-dd’T’HH:mm:ss.SSSZ)")
                    Date createdOnBefore,
            @RequestParam(name = "modifiedAfter", required = false)
            @ApiParam(value = "Content modified after the given date and time (yyyy-MM-dd’T’HH:mm:ss.SSSZ)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date modifiedOnAfter,
            @RequestParam(name = "modifiedBefore", required = false)
            @ApiParam(value = "Content modified before the given date and time (yyyy-MM-dd’T’HH:mm:ss.SSSZ)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date modifiedOnBefore,
            @RequestHeader(value="Request-Context", required=false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext,
            Pageable pageable) {

        ArrayList<SqlCriteria> params = new ArrayList<SqlCriteria>();

        // Build a Content object with the values passed in
        Content content = new Content();

        if (studioId != null) {
            Studio studio = studioRepository.findOne(studioId);
            if (studio == null) {
                return new ResponseEntity(new ApiError("Studio id specified not found."), HttpStatus.BAD_REQUEST);
            } else {
                params.add(new SqlCriteria("studio", ":", studioId));
            }
        }

        if (eidr != null) {
            params.add(new SqlCriteria("eidr", ":", eidr));
        }

        if (eidrv != null) {
            params.add(new SqlCriteria("eidrv", ":", eidrv));
        }

        if (gtm != null) {
            params.add(new SqlCriteria("gtm", ":", gtm));
        }
        if (title != null) {
            params.add(new SqlCriteria("title", ":", title));
        }

        if (status != null) {
            params.add(new SqlCriteria("status", ":", status));
        }

        if (createdOnAfter != null) {
            params.add(new SqlCriteria("createdOn", ">", createdOnAfter));
        }
        if (createdOnBefore != null) {
            params.add(new SqlCriteria("createdOn", "<", createdOnBefore));
        }
        if (modifiedOnAfter != null) {
            params.add(new SqlCriteria("modifiedOn", ">", modifiedOnAfter));
        }
        if (modifiedOnBefore != null) {
            params.add(new SqlCriteria("modifiedOn", "<", modifiedOnBefore));
        }

        List<Specification<Content>> specs = new ArrayList<>();
        for (SqlCriteria param : params) {
            specs.add(new ContentSpecification(param));
        }

        Page<Content> contents;
        if (params.isEmpty()) {
            contents = contentRepository.findAll(pageable);
        } else {
            Specification<Content> query = specs.get(0);
            for (int i = 1; i < specs.size(); i++) {
                query = Specifications.where(query).and(specs.get(i));
            }
            contents = contentRepository.findAll(query, pageable);
        }

        return new ResponseEntity<Page<Content>>(contents, HttpStatus.OK);
    }

}
