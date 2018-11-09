package com.universalinvents.udccs.external;

public class ExternalAWSCredentialsRequest {

    private String grantType = "client_credentials";

    public ExternalAWSCredentialsRequest() {}

    public ExternalAWSCredentialsRequest(String grantType) {
        this.grantType = grantType;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
