package com.dreamworks.uddcs.pairings;

/**
 * Created by dsherman on 2/27/17.
 */
public class PairMasterCodeRequest
{
    private Long retailerId;
    private String pairedBy;
    private String status;

    public PairMasterCodeRequest() {}

    public PairMasterCodeRequest(Long retailerId, String pairedBy, String status)
    {
        this.retailerId = retailerId;
        this.pairedBy = pairedBy;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
