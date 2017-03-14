package com.dreamworks.uddcs.contents;

/**
 * Created by dsherman on 2/27/17.
 */
public class AddRetailerToContentRequest
{
    private String retailerId;

    public AddRetailerToContentRequest() {}

    public AddRetailerToContentRequest(String retailerId)
    {
        this.retailerId = retailerId;
    }

    public String getRetailerId()
    {
        return retailerId;
    }

    public void setRetailerId(String retailerId)
    {
        this.retailerId = retailerId;
    }
}
