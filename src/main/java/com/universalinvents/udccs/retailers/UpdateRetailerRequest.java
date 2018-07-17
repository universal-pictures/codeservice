package com.universalinvents.udccs.retailers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 */
@ApiModel("UpdateRetailerRequest")
public class UpdateRetailerRequest {
    @ApiModelProperty(value = "The name of the retailer",
                      example = "Movies Anywhere",
                      required = false)
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
                      required = false)
    private String redemptionUrl;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = false)
    private String status;
    @ApiModelProperty(value = "A field used to store any external identifier related to this retailer",
            example = "123-53242-3234eff-2234f-231232",
            required = false)
    private String externalId;
    @ApiModelProperty(value="The base URL for this retailer's Code Management Service API",
            example = "https://example.com",
            required = false)
    private String baseUrl;

    public UpdateRetailerRequest() {}

    public UpdateRetailerRequest(String name, Boolean generateCodes, String logoUrl, String redemptionUrl,
                                 String status, String externalId, String baseUrl) {
        this.name = name;
        this.generateCodes = generateCodes;
        this.logoUrl = logoUrl;
        this.redemptionUrl = redemptionUrl;
        this.status = status;
        this.externalId = externalId;
        this.baseUrl = baseUrl;
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

    public String getExternalId() { return externalId; }

    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getBaseUrl() { return baseUrl; }

    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
}
