package com.universalinvents.udccs.retailers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 */
@ApiModel("UpdateRetailerRequest")
public class UpdateRetailerRequest
{
    @ApiModelProperty(value = "The name of the retailer",
                      example = "Movies Anywhere",
                      required = false)
    private String name;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = false)
    private String status;

    public UpdateRetailerRequest() {}

    public UpdateRetailerRequest(String name, String status)
    {
        this.name = name;
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
