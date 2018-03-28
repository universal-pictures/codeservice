package com.universalinvents.udccs.retailers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 */
@ApiModel("CreateRetailerRequest")
public class CreateRetailerRequest
{
    @ApiModelProperty(value = "The name of the retailer",
                      example = "Movies Anywhere",
                      required = true)
    private String name;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = true)
    private String status;

    public CreateRetailerRequest() {}

    public CreateRetailerRequest(String name, String status)
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
