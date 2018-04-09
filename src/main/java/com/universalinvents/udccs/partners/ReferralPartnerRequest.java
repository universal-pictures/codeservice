package com.universalinvents.udccs.partners;

import java.util.Set;

public interface ReferralPartnerRequest {

    public String getName();
    public void setName(String name);

    public String getDescription();
    public void setDescription(String description);

    public String getContactName();
    public void setContactName(String contactName);

    public String getContactEmail();
    public void setContactEmail(String contactEmail);

    public String getContactPhone();
    public void setContactPhone(String contactPhone);

    public Set<Long> getRetailerIds();
    public void setRetailerIds(Set<Long> retailerIds);

    public String getStatus();
    public void setStatus(String status);
}
