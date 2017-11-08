package com.dreamworks.uddcs.contents;

import com.dreamworks.uddcs.exception.ApiError;
import com.dreamworks.uddcs.retailers.Retailer;
import com.dreamworks.uddcs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {
    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @CrossOrigin
    @ApiOperation("Create a Content Entry")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Content> createContent(@RequestBody ContentRequest request) {
        Content content = new Content();
        content.setTitle(request.getTitle());
        content.setEidr(request.getEidr());
        content.setEidrv(request.getEidrv());
        content.setGtm(request.getGtm());
        content.setStatus(request.getStatus());
        content.setMsrp(request.getMsrp());

        contentRepository.save(content);

        return new ResponseEntity<Content>(content, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation("Get Content information for a given Content id")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        Content content = contentRepository.findOne(id);
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        return new ResponseEntity<Content>(content, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Get Content List")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Content>> getContents() {
        List<Content> contents = contentRepository.findAll();
        return new ResponseEntity<List<Content>>(contents, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation("Add a Retailer for Content")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Content> addRetailerToContent(@PathVariable Long id,
                                                        @RequestBody AddRetailerToContentRequest request) {
        Retailer retailer = retailerRepository.findOne(request.getRetailerId());
        if (retailer == null)
            return new ResponseEntity(new ApiError("Retailer id expressed is not found."), HttpStatus.NOT_FOUND);


        Content content = contentRepository.findOne(id);
        if (content == null)
            return new ResponseEntity(new ApiError("Content id expressed is not found."), HttpStatus.NOT_FOUND);

        retailer.getContents().add(content);
        content.getRetailers().add(retailer);

        contentRepository.save(content);

        return new ResponseEntity<Content>(content, HttpStatus.CREATED);
    }
}
