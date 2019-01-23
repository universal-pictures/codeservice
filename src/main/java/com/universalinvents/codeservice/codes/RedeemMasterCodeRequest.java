package com.universalinvents.codeservice.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class RedeemMasterCodeRequest
{
    private String redeemedBy;

    public RedeemMasterCodeRequest() {}

    public RedeemMasterCodeRequest(String redeemedBy)
    {
        this.redeemedBy = redeemedBy;
    }

    public String getRedeemedBy()
    {
        return redeemedBy;
    }

    public void setRedeemedBy(String redeemedBy)
    {
        this.redeemedBy = redeemedBy;
    }
}
