package com.universalinvents.udccs.contents;

/**
 * Created by dsherman on 2/27/17.
 */
public class AddRetailerToContentRequest
{
    private Long retailerId;

    public AddRetailerToContentRequest() {}

    public AddRetailerToContentRequest(Long retailerId)
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
