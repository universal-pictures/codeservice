package com.universalinvents.codeservice.partners;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@ApiModel("CreateReferralPartnerRequest")
public class CreateReferralPartnerRequest implements ReferralPartnerRequest {

    @ApiModelProperty(value = "The name of the Referral Partner",
                      example = "My Disrupting Tech",
                      required = true)
    private String name;
    @ApiModelProperty(value = "A simple description of the Referral Partner",
                      example = "We will disrupt all industries with our tech!",
                      required = true)
    private String description;
    @ApiModelProperty(value = "The name of a contact at the Referral Partner",
                      example = "Jane Doe",
                      required = false)
    private String contactName;
    @ApiModelProperty(value = "The email address of the contact at the Referral Partner",
                      example = "janedoe@mydomain.com",
                      required = false)
    private String contactEmail;
    @ApiModelProperty(value = "The phone number of the contact at the Referral Partner",
                      example = "+1-555-555-1234",
                      required = false)
    private String contactPhone;
    @ApiModelProperty(value = "A list of Retailer id's that this Referral Partner has business relations with",
                      example = "[1, 2]",
                      required = true)
    private Set<Long> retailerIds;
    @ApiModelProperty(value = "A list of Studio id's that this Referral Partner has business relations with",
                      example = "[1, 2]",
                      required = true)
    private Set<Long> studioIds;
    @ApiModelProperty(value = "A URL pointing to a logo for this Referral Partner",
                      example = "http://example.com/mylogoimage.png",
                      required = false)
    private String logoUrl;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = true)
    private String status;

    public CreateReferralPartnerRequest() {
    }

    public CreateReferralPartnerRequest(String name, String description, String contactName, String contactEmail,
                                        String contactPhone, Set<Long> retailerIds, Set<Long> studioIds, String logoUrl,
                                        String status) {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.retailerIds = retailerIds;
        this.studioIds = studioIds;
        this.logoUrl = logoUrl;
        this.status = status;
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

    public Set<Long> getRetailerIds() {
        return retailerIds;
    }

    public void setRetailerIds(Set<Long> retailerIds) {
        this.retailerIds = retailerIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Long> getStudioIds() {
        return studioIds;
    }

    public void setStudioIds(Set<Long> studioIds) {
        this.studioIds = studioIds;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
