package com.dreamworks.uddcs.retailers;

/**
 * Created by dsherman on 2/27/17.
 */
public class RetailerRequest
{
    private String name;
    private String regionCode;

    public RetailerRequest() {}

    public RetailerRequest(String name, String regionCode)
    {
        this.name = name;
        this.regionCode = regionCode;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRegionCode()
    {
        return regionCode;
    }

    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }
}
