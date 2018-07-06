package com.universalinvents.udccs.external;

public class ExternalRetailerCodeRequest {

    private String eidr;

    public ExternalRetailerCodeRequest() {}

    public ExternalRetailerCodeRequest(String eidr) {
        this.eidr = eidr;
    }

    public String getEidr() { return eidr; }

    public void setEidr(String eidr) { this.eidr = eidr; }
}
