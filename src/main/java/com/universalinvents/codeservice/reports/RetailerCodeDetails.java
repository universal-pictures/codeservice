package com.universalinvents.codeservice.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetailerCodeDetails {

    @JsonProperty("PAIRED")
    private long paired;
    @JsonProperty("REDEEMED")
    private long redeemed;
    @JsonProperty("EXPIRED")
    private long expired;
    @JsonProperty("ZOMBIED")
    private long zombied;

    public RetailerCodeDetails() {}

    public RetailerCodeDetails(long paired, long redeemed, long expired, long zombied) {
        this.paired = paired;
        this.redeemed = redeemed;
        this.expired = expired;
        this.zombied = zombied;
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

    public long getZombied() {
        return zombied;
    }

    public void setZombied(long zombied) {
        this.zombied = zombied;
    }
}
