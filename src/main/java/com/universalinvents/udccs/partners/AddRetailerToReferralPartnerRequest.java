package com.universalinvents.udccs.partners;

/**
 * Created by dsherman on 2/27/17.
 */
public class AddRetailerToReferralPartnerRequest
{
    private Long retailerId;

    public AddRetailerToReferralPartnerRequest() {}

    public AddRetailerToReferralPartnerRequest(Long retailerId)
    {
        this.retailerId = retailerId;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }
}
