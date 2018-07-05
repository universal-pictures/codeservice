package com.universalinvents.udccs.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetailerCodeDetails {

    @JsonProperty("PAIRED")
    private long paired;
    @JsonProperty("REDEEMED")
    private long redeemed;

    public RetailerCodeDetails() {}

    public RetailerCodeDetails(long paired, long redeemed) {
        this.paired = paired;
        this.redeemed = redeemed;
    }

    public long getPaired() {
        return paired;
    }

    public void setPaired(long paired) {
        this.paired = paired;
    }

    public long getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(long redeemed) {
        this.redeemed = redeemed;
    }

}
