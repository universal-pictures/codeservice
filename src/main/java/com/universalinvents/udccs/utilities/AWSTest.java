package com.universalinvents.udccs.utilities;

import com.universalinvents.udccs.external.ExternalAWSCredentialsRequest;
import com.universalinvents.udccs.external.ExternalRetailerCodeResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Base64;

public class AWSTest {

    private static void main(String[] argv) {

//        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.defaultClient();
//        stsClient.setEndpoint();

        String auth = "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        String url = tokenBaseUrl + "/oauth2/token";
        ExternalAWSCredentialsRequest request = new ExternalAWSCredentialsRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        headers.add(HttpHeaders.AUTHORIZATION, auth);
        HttpEntity<ExternalAWSCredentialsRequest> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, ExternalRetailerCodeResponse.class);
        } catch (Exception e) {
            // If anything goes wrong here, simply ignore it and allow
            // the EST Inventory service status to get out of sync.  There will
            // be a separate scheduler that runs periodically to fix these sync issues
            // if any are found.

            // NOOP
        }
    }
}
