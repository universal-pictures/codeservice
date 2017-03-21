package com.dreamworks.uddcs.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class PairStudioCodeRequest
{
    private Long retailerId;
    private String pairedBy;

    public PairStudioCodeRequest() {}

    public PairStudioCodeRequest(Long retailerId, String pairedBy)
    {
        this.retailerId = retailerId;
        this.pairedBy = pairedBy;
    }

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
