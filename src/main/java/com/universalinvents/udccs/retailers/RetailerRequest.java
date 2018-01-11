package com.universalinvents.udccs.retailers;

/**
 * Created by dsherman on 2/27/17.
 */
public class RetailerRequest
{
    private String name;
    private String regionCode;
    private String status;

    public RetailerRequest() {}

    public RetailerRequest(String name, String regionCode, String status)
    {
        this.name = name;
        this.regionCode = regionCode;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
