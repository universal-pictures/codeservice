package com.universalinvents.udccs.retailers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 */
@ApiModel("CreateRetailerRequest")
public class CreateRetailerRequest {
    @ApiModelProperty(value = "The name of the retailer",
                      example = "Movies Anywhere",
                      required = true)
    private String name;
    @ApiModelProperty(value = "Does this retailer allow for dynamic code generation? (Default is false)",
                      example = "false",
                      required = false)
    private Boolean generateCodes = Boolean.FALSE;
    @ApiModelProperty(value = "A URL pointing to a logo for this retailer",
                      example = "http://example.com/mylogoimage.png",
                      required = false)
    private String logoUrl;
    @ApiModelProperty(value = "Where to send users when redeeming codes for this retailer",
                      example = "http://example.com/redeem/",
                      required = true)
    private String redemptionUrl;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = true)
    private String status;

    public CreateRetailerRequest() {}

    public CreateRetailerRequest(String name, Boolean generateCodes, String logoUrl, String redemptionUrl,
                                 String status) {
        this.name = name;
        this.generateCodes = generateCodes;
        this.logoUrl = logoUrl;
        this.redemptionUrl = redemptionUrl;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getGenerateCodes() {
        return generateCodes;
    }

    public void setGenerateCodes(Boolean generateCodes) {
        this.generateCodes = generateCodes;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getRedemptionUrl() {
        return redemptionUrl;
    }

    public void setRedemptionUrl(String redemptionUrl) {
        this.redemptionUrl = redemptionUrl;
    }
}
