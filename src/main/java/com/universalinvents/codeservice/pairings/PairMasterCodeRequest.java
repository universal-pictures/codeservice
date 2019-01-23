package com.universalinvents.codeservice.pairings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("PairMasterCodeRequest")
public class PairMasterCodeRequest
{
    @ApiModelProperty(value = "The id of the Retailer from which to find a code to pair with a Master Code",
                      example = "1",
                      required = true)
    private Long retailerId;
    @ApiModelProperty(value = "Who's executing this request? Value can be any string but an external customer " +
                              "id is preferred.",
                      example = "qas8fcw00sddergwpjkn4weiibnc925z",
                      required = false)
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
