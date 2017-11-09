package com.dreamworks.uddcs.contents;

import java.math.BigDecimal;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
public class ContentRequest
{
    private String title;
    private String eidr;
    private String eidrv;
    private String gtm;
    private String status;
    private BigDecimal msrp;

    public ContentRequest () {}

    public ContentRequest(String title, String eidr, String eidrv, String gtm, String status, BigDecimal msrp)
    {
        this.title = title;
        this.eidr = eidr;
        this.eidrv = eidrv;
        this.gtm = gtm;
        this.status = status;
        this.msrp = msrp;
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

    public String getEidrv() {
        return eidrv;
    }

    public void setEidrv(String eidrv) {
        this.eidrv = eidrv;
    }

    public String getGtm()
    {
        return gtm;
    }

    public void setGtm(String gtm)
    {
        this.gtm = gtm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }
}
