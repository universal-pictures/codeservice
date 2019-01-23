package com.universalinvents.codeservice.reports;

import java.util.List;

public class RetailerDetails {

    private String name;
    private RetailerCodeDetails retailerCodes;
    private List<FormatDetails> formats;

    public RetailerDetails() {}

    public RetailerDetails(String name, RetailerCodeDetails retailerCodes, List<FormatDetails> formats) {
        this.name = name;
        this.retailerCodes = retailerCodes;
        this.formats = formats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RetailerCodeDetails getRetailerCodes() {
        return retailerCodes;
    }

    public void setRetailerCodes(RetailerCodeDetails retailerCodes) {
        this.retailerCodes = retailerCodes;
    }

    public List<FormatDetails> getFormats() {
        return formats;
    }

    public void setFormats(List<FormatDetails> formats) {
        this.formats = formats;
    }
}
