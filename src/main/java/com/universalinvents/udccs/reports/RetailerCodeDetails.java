package com.universalinvents.udccs.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetailerCodeDetails {

    @JsonProperty("UNALLOCATED")
    private long unallocated;
    @JsonProperty("PAIRED")
    private long paired;
    @JsonProperty("REDEEMED")
    private long redeemed;

    public RetailerCodeDetails() {}

    public RetailerCodeDetails(long unallocated, long paired, long redeemed) {
        this.unallocated = unallocated;
        this.paired = paired;
        this.redeemed = redeemed;
    }

    public long getUnallocated() {
        return unallocated;
    }

    public void setUnallocated(long unallocated) {
        this.unallocated = unallocated;
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
