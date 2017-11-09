package com.dreamworks.uddcs.apps;

/**
 * Created by kkirkland on 11/7/17.
 */
public class AppRequest {

    private String name;
    private String description;
    private String status;
    private Long partnerId;

    public AppRequest() {
    }

    public AppRequest(String name, String description, String status, Long partnerId) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.partnerId = partnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
}
