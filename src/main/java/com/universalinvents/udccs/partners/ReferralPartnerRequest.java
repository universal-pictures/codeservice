package com.universalinvents.udccs.partners;

import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
public class ReferralPartnerRequest {
    private String name;
    private String description;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private Set<Long> retailerIds;
    private String status;

    public ReferralPartnerRequest() {
    }

    public ReferralPartnerRequest(String name, String description, String contactName, String contactEmail,
                                  String contactPhone, Set<Long> retailerIds, String status) {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.retailerIds = retailerIds;
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
}
