package com.universalinvents.udccs.reports;

import java.util.List;

public class PartnerDetails {

    private String name;
    private MasterCodeDetails masterCodes;
    private List<AppDetails> apps;

    public PartnerDetails() {}

    public PartnerDetails(String name, MasterCodeDetails masterCodes, List<AppDetails> apps) {
        this.name = name;
        this.masterCodes = masterCodes;
        this.apps = apps;
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

    public List<AppDetails> getApps() {
        return apps;
    }

    public void setApps(List<AppDetails> apps) {
        this.apps = apps;
    }
}
