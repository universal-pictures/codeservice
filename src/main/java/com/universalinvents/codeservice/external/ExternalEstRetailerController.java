package com.universalinvents.codeservice.external;

import com.universalinvents.codeservice.codes.RetailerCode;
import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.exception.HttpException;
import com.universalinvents.codeservice.exception.RecordNotFoundException;
import com.universalinvents.codeservice.retailers.Retailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

// Provide centralized functionality for all EST Inventory Service calls
@Component
public class ExternalEstRetailerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    @Value("${aws.security.client_id}")
    private String clientId;

    @Value("${aws.security.client_secret}")
    private String clientSecret;

    @Value("${aws.security.token_base_url}")
    private String tokenBaseUrl;

    private final Logger log = LoggerFactory.getLogger(ExternalEstRetailerController.class);

    public ExternalEidrMappingsResponse inventoryCount(Retailer retailer, String encodedEidr, String requestContext) {
        String baseUrl = retailer.getBaseUrl();
        if (baseUrl != null) {
            // Get an AWS authorization token first
            String authToken;
            try {
                authToken = getAWSCredentials();
            } catch (Exception e) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }

            URI url = URI.create(baseUrl + "/eidrMappings/" + encodedEidr + "/inventoryCount");
            log.debug("Calling " + url);
            HttpHeaders headers = setHeaders(authToken, requestContext);
            HttpEntity entity = new HttpEntity(null, headers);

            return (retryTemplate.execute(context -> (restTemplate.exchange(url, HttpMethod.GET,
                    entity, ExternalEidrMappingsResponse.class).getBody())));


        }

        return null;
    }

    public ExternalRetailerCodeResponse fetchRetailerCode(Content content, Retailer retailer, String requestContext)
            throws RecordNotFoundException, HttpException {
        if (retailer.getBaseUrl() != null) {
            // Get an AWS authorization token first
            String authToken;
            try {
                authToken = getAWSCredentials();
            } catch (Exception e) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }

            String url = retailer.getBaseUrl() + "/retailerCodes";
            log.debug("Calling " + url);
            ExternalRetailerCodeRequest request = new ExternalRetailerCodeRequest(content.getEidr(), null);
            HttpHeaders headers = setHeaders(authToken, requestContext);
            HttpEntity<ExternalRetailerCodeRequest> entity = new HttpEntity<>(request, headers);

            return (retryTemplate.execute(context -> (restTemplate.exchange(url, HttpMethod.POST, entity,
                    ExternalRetailerCodeResponse.class).getBody())));
        }

        throw new HttpException(retailer.getName() + " does not have a base URL set.  Unable to " +
                "get a Retailer Code.");
    }

    public <T> T redeem(RetailerCode retailerCode, String requestContext, Class<T> returnClass) {
        String retailerBaseUrl = retailerCode.getRetailer().getBaseUrl();
        if (retailerBaseUrl != null) {
            // Get an AWS authorization token first
            String authToken = null;
            try {
                authToken = getAWSCredentials();
            } catch (Exception e) {
                // If we weren't able to get an auth token, simply ignore it and allow
                // the EST Inventory service status to get out of sync.  There will
                // be a separate scheduler that runs periodically to fix these sync issues
                // if any are found.
            }

            if (authToken != null) {
                String url = retailerBaseUrl + "/retailerCodes/{code}/redeem";
                log.debug("Calling " + url);
                Map<String, String> pathParams = new HashMap<>();
                pathParams.put("code", retailerCode.getCode());
                HttpHeaders headers = setHeaders(authToken, requestContext);
                HttpEntity entity = new HttpEntity(headers);

                try {
                    return (returnClass.cast(retryTemplate.execute(context -> (restTemplate.exchange(url,
                            HttpMethod.PUT, entity, returnClass, pathParams).getBody()))));
                } catch (Exception e) {
                    // If anything goes wrong here, simply ignore it and allow
                    // the EST Inventory service status to get out of sync.  There will
                    // be a separate scheduler that runs periodically to fix these sync issues
                    // if any are found.

                    // NOOP
                    log.warn("Unable to redeem retailer code " + retailerCode.getCode() + " using " + url +
                            " : " + e.getMessage());
                }
            }
        }
        return (null);
    }

    public <T> T status(RetailerCode retailerCode, String requestContext, Class<T> returnClass)
            throws HttpClientErrorException, HttpServerErrorException, RestClientException, HttpException {
        Retailer retailer = retailerCode.getRetailer();
        if (retailer.getBaseUrl() != null) {
            // Get an AWS authorization token first
            String authToken;
            try {
                authToken = getAWSCredentials();
            } catch (Exception e) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }

            String url = retailer.getBaseUrl() + "/retailerCodes/{code}/status/refresh";
            log.debug("Calling " + url);
            Map<String, String> pathParams = new HashMap<>();
            pathParams.put("code", retailerCode.getCode());
            HttpHeaders headers = setHeaders(authToken, requestContext);
            HttpEntity entity = new HttpEntity(headers);

            return (retryTemplate.execute(context -> (restTemplate.exchange(url, HttpMethod.GET, entity,
                    returnClass, pathParams).getBody())));
        }

        throw new HttpException(retailer.getName() + " does not have a base URL set.  Unable to " +
                "get the status.");
    }

    public void killRetailerCode(RetailerCode retailerCode, String requestContext)
            throws HttpException, HttpClientErrorException {
        if (retailerCode.getRetailer().getBaseUrl() != null) {
            // Get an AWS authorization token first
            String authToken;
            try {
                authToken = getAWSCredentials();
            } catch (Exception e) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }

            String url = retailerCode.getRetailer().getBaseUrl() + "/retailerCodes/{code}/kill";
            log.debug("Calling " + url);
            Map<String, String> pathParams = new HashMap<>();
            pathParams.put("code", retailerCode.getCode());
            HttpHeaders headers = setHeaders(authToken, requestContext);
            HttpEntity entity = new HttpEntity(headers);

            retryTemplate.execute(context -> (restTemplate.exchange(url, HttpMethod.PUT, entity,
                    ExternalRetailerCodeResponse.class, pathParams)));

            return;
        }

        throw new HttpException(retailerCode.getRetailer().getName() + " does not have a base URL set.  Unable to " +
                "kill the Retailer Code.");
    }


    private HttpHeaders setHeaders(String authToken, String requestContext) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, authToken);
        headers.set("Request-Context", requestContext);
        return (headers);
    }

    private String getAWSCredentials() {
        String auth = "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        String url = tokenBaseUrl + "/oauth2/token";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, auth);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        String retAccessToken;
        try {
            log.debug("Calling " + url);
            ResponseEntity<ExternalAWSCredentialsResponse> resp = retryTemplate.execute(context -> (
                    restTemplate.exchange(url, HttpMethod.POST, entity, ExternalAWSCredentialsResponse.class)));
            retAccessToken = resp.getBody().getAccess_token();
            log.debug("Received access token " + retAccessToken);

        } catch (Exception e) {
            log.warn("Unable to get AWS credentials: " + e.getMessage(), e);
            throw e;
        }
        return retAccessToken;
    }

}
