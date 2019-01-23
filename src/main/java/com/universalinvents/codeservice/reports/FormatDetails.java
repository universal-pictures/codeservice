package com.universalinvents.codeservice.reports;

public class FormatDetails {

    private String name;
    private RetailerCodeDetails retailerCodes;

    public FormatDetails() {}

    public FormatDetails(String name, RetailerCodeDetails retailerCodes) {
        this.name = name;
        this.retailerCodes = retailerCodes;
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
}
