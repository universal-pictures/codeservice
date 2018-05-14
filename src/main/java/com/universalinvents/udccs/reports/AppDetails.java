package com.universalinvents.udccs.reports;

public class AppDetails {

    private String name;
    private MasterCodeDetails masterCodes;

    public AppDetails() {}

    public AppDetails(String name, MasterCodeDetails masterCodes) {
        this.name = name;
        this.masterCodes = masterCodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MasterCodeDetails getMasterCodes() {
        return masterCodes;
    }

    public void setMasterCodes(MasterCodeDetails masterCodes) {
        this.masterCodes = masterCodes;
    }
}
