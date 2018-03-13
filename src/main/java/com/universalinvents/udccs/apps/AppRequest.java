package com.universalinvents.udccs.apps;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by kkirkland on 11/7/17.
 */
@ApiModel("AppRequest")
public class AppRequest {

    @ApiModelProperty(value="The name of the app (i.e. iOS App)")
    private String name;
    @ApiModelProperty(value="A short description of the app")
    private String description;
    @ApiModelProperty(value="ACTIVE or INACTIVE")
    private String status;
    @ApiModelProperty(value="The id of the related Referral Partner")
    private Long partnerId;
    @ApiModelProperty(value="Unique api access token")
    private String accessToken;

    public AppRequest() {
    }

    public AppRequest(String name, String description, String status, Long partnerId, String accessToken) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.partnerId = partnerId;
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
