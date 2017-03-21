package com.dreamworks.uddcs.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class PairStudioCodeRequest
{
//    private String retailerCode;
    private Long retailerId;
    private String pairedBy;

    public PairStudioCodeRequest() {}

    public PairStudioCodeRequest(String code, /**String retailerCode,**/ Long retailerId, String pairedBy)
    {
//        this.retailerCode = retailerCode;
        this.retailerId = retailerId;
        this.pairedBy = pairedBy;
    }

//    public String getRetailerCode()
//    {
//        return retailerCode;
//    }
//
//    public void setRetailerCode(String retailerCode)
//    {
//        this.retailerCode = retailerCode;
//    }

    public String getPairedBy()
    {
        return pairedBy;
    }

    public void setPairedBy(String pairedBy)
    {
        this.pairedBy = pairedBy;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }
}
