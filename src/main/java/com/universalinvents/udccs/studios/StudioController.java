package com.universalinvents.udccs.studios;

import com.universalinvents.udccs.exception.ApiError;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        studioRepository.save(studio);

        return new ResponseEntity<Studio>(studio, HttpStatus.CREATED);
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
}
