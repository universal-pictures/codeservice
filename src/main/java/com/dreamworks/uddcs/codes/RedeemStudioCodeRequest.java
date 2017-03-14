package com.dreamworks.uddcs.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class RedeemStudioCodeRequest
{
    private String redeemedBy;

    public RedeemStudioCodeRequest() {}

    public RedeemStudioCodeRequest(String redeemedBy)
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
