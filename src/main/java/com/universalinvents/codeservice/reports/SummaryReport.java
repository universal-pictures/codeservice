package com.universalinvents.codeservice.reports;

public class SummaryReport {
    private long studios;
    private long partners;
    private long retailers;
    private long contents;

    public SummaryReport() {}

    public SummaryReport(long studios, long partners, long retailers, long contents) {
        this.studios = studios;
        this.partners = partners;
        this.retailers = retailers;
        this.contents = contents;
    }

    public long getStudios() {
        return studios;
    }

    public void setStudios(long studios) {
        this.studios = studios;
    }

    public long getPartners() {
        return partners;
    }

    public void setPartners(long partners) {
        this.partners = partners;
    }

    public long getRetailers() {
        return retailers;
    }

    public void setRetailers(long retailers) {
        this.retailers = retailers;
    }

    public long getContents() {
        return contents;
    }

    public void setContents(long contents) {
        this.contents = contents;
    }
}
