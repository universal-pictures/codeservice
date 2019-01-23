package com.universalinvents.codeservice.retailers;

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
    @ApiModelProperty(value = "A field used to store any external identifier related to this retailer",
                      example = "123-53242-3234eff-2234f-231232",
                      required = false)
    private String externalId;
    @ApiModelProperty(value="The base URL for this retailer's Code Management Service API",
                      example = "https://example.com",
                      required = false)
    private String baseUrl;

    public CreateRetailerRequest() {}

    public CreateRetailerRequest(String name, String logoUrl, String redemptionUrl,
                                 String status, String externalId, String baseUrl) {
        this.name = name;
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
