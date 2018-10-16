package com.universalinvents.udccs.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalRetailerCodeResponse {

    private String code;
    private LocalDateTime expiresOn;
    private String status;

    public ExternalRetailerCodeResponse() {}

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX")
    public LocalDateTime getExpiresOn() { return expiresOn; }

    public void setExpiresOn(LocalDateTime expiresOn) { this.expiresOn = expiresOn; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
