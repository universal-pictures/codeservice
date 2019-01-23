package com.universalinvents.codeservice.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterCodeDetails {

    @JsonProperty("ISSUED")
    private long issued;
    @JsonProperty("PAIRED")
    private long paired;
    @JsonProperty("REDEEMED")
    private long redeemed;
    @JsonProperty("EXPIRED")
    private long expired;

    public MasterCodeDetails() {}

    public MasterCodeDetails(long issued, long paired, long redeemed, long expired) {
        this.issued = issued;
        this.paired = paired;
        this.redeemed = redeemed;
        this.expired = expired;
    }

    public long getIssued() {
        return issued;
    }

    public void setIssued(long issued) {
        this.issued = issued;
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

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }
}
