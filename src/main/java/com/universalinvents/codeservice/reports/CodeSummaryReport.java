package com.universalinvents.codeservice.reports;

public class CodeSummaryReport {

    private MasterCodeDetails masterCodes;
    private RetailerCodeDetails retailerCodes;

    public CodeSummaryReport() {}

    public CodeSummaryReport(MasterCodeDetails masterCodes, RetailerCodeDetails retailerCodes) {
        this.masterCodes = masterCodes;
        this.retailerCodes = retailerCodes;
    }

    public MasterCodeDetails getMasterCodes() {
        return masterCodes;
    }

    public void setMasterCodes(MasterCodeDetails masterCodes) {
        this.masterCodes = masterCodes;
    }

    public RetailerCodeDetails getRetailerCodes() {
        return retailerCodes;
    }

    public void setRetailerCodes(RetailerCodeDetails retailerCodes) {
        this.retailerCodes = retailerCodes;
    }
}
