package com.dreamworks.uddcs.pairings;

/**
 * Created by dsherman on 2/27/17.
 */
public class PairMasterCodeRequest
{
    private Long retailerId;
    private String pairedBy;

    public PairMasterCodeRequest() {}

    public PairMasterCodeRequest(Long retailerId, String pairedBy)
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
