package com.universalinvents.udccs.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterCodeDetails {

    @JsonProperty("ISSUED")
    private long issued;
    @JsonProperty("UNALLOCATED")
    private long unallocated;
    @JsonProperty("PAIRED")
    private long paired;
    @JsonProperty("REDEEMED")
    private long redeemed;

    public MasterCodeDetails() {}

    public MasterCodeDetails(long issued, long unallocated, long paired, long redeemed) {
        this.issued = issued;
        this.unallocated = unallocated;
        this.paired = paired;
        this.redeemed = redeemed;
    }

    public long getIssued() {
        return issued;
    }

    public void setIssued(long issued) {
        this.issued = issued;
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
