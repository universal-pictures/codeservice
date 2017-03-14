package com.dreamworks.uddcs.contents;

/**
 * Created by dsherman on 2/27/17.
 */
public class ContentRequest
{
    private String title;
    private String eidr;
    private String gtm;

    public ContentRequest () {}

    public ContentRequest(String title, String eidr, String gtm)
    {
        this.title = title;
        this.eidr = eidr;
        this.gtm = gtm;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getEidr()
    {
        return eidr;
    }

    public void setEidr(String eidr)
    {
        this.eidr = eidr;
    }

    public String getGtm()
    {
        return gtm;
    }

    public void setGtm(String gtm)
    {
        this.gtm = gtm;
    }
}
