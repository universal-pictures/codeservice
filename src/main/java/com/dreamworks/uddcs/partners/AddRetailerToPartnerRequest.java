package com.dreamworks.uddcs.partners;

/**
 * Created by dsherman on 2/27/17.
 */
public class AddRetailerToPartnerRequest
{
    private Long retailerId;

    public AddRetailerToPartnerRequest() {}

    public AddRetailerToPartnerRequest(Long retailerId)
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
