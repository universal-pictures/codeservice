package com.universalinvents.codeservice.external;

public class ExternalRetailerCodeRequest {

    private String eidr;
    private String code;

    public ExternalRetailerCodeRequest() {}

    public ExternalRetailerCodeRequest(String eidr, String code) {
        this.eidr = eidr;
        this.code = code;
    }

    public String getEidr() { return eidr; }

    public void setEidr(String eidr) { this.eidr = eidr; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }
}
