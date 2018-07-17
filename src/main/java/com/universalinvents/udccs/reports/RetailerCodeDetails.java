package com.universalinvents.udccs.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetailerCodeDetails {

    @JsonProperty("PAIRED")
    private long paired;
    @JsonProperty("REDEEMED")
    private long redeemed;
    @JsonProperty("EXPIRED")
    private long expired;

    public RetailerCodeDetails() {}

    public RetailerCodeDetails(long paired, long redeemed, long expired) {
        this.paired = paired;
        this.redeemed = redeemed;
        this.expired = expired;
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

    public long getExpired() { return expired; }

    public void setExpired(long expired) { this.expired = expired; }

}
