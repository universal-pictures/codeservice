package com.universalinvents.udccs.studios;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by kkirkland on 11/8/17.
 */
@ApiModel("CreateStudioRequest")
public class CreateStudioRequest {

    @ApiModelProperty(value = "The name of the studio",
                      example = "Universal Pictures",
                      required = true)
    private String name;
    @ApiModelProperty(value = "A short description of the studio",
                      example = "Universal Pictures is an American film studio, owned by Comcast through its wholly " +
                              "owned subsidiary NBCUniversal, and is one of Hollywood's \"Big Six\" film studios.",
                      required = true)
    private String description;
    @ApiModelProperty(value = "The name of a contact at the Studio",
                      example = "Jane Doe",
                      required = false)
    private String contactName;
    @ApiModelProperty(value = "The email address of the contact at the Studio",
                      example = "janedoe@mydomain.com",
                      required = false)
    private String contactEmail;
    @ApiModelProperty(value = "The phone number of the contact at the Studio",
                      example = "+1-555-555-1234",
                      required = false)
    private String contactPhone;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = true)
    private String status;
    @ApiModelProperty(value = "A placeholder field to store bitwise flags for future use",
                      example = "1024",
                      required = false)
    private Long flags;
    @ApiModelProperty(value = "A 2 character prefix to prepend to all Master Codes generated for Content from " +
                              "this Studio.",
                      example = "UP",
                      required = true)
    private String codePrefix;

    public CreateStudioRequest() {
    }

    public CreateStudioRequest(String name, String description, String contactName, String contactEmail, String contactPhone,
                               String status, Long flags, String codePrefix) {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.status = status;
        this.flags = flags;
        this.codePrefix = codePrefix;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public String getCodePrefix() {
        return codePrefix;
    }

    public void setCodePrefix(String codePrefix) {
        this.codePrefix = codePrefix;
    }
}
