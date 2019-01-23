package com.universalinvents.codeservice.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CodeServiceHttpLoggingFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(CodeServiceHttpLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // Don't run this filter on things unrelated to our api
        // (like Swagger UI)
        String includedUrl = "/api/";
        if (!((HttpServletRequest) request).getServletPath().startsWith(includedUrl)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper respWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        filterChain.doFilter(requestWrapper, respWrapper);

        // Log the request
        if (log.isDebugEnabled()) {
            // Lowercase all headers
            HashMap<String, String> headers = new HashMap<>();
            for (String header : Collections.list(requestWrapper.getHeaderNames())) {
                headers.put(header, requestWrapper.getHeader(header).toLowerCase());
            }

            String body = new String(requestWrapper.getContentAsByteArray()).replaceAll("\\R", "");
            log.debug(buildRequestMessage(requestWrapper.getMethod(), requestWrapper.getRequestURI(),
                    headers, requestWrapper.getParameterMap(), body));

        }

        // Log the response
        String content = new String(respWrapper.getContentAsByteArray());
        if (respWrapper.getStatusCode() >= 200 && respWrapper.getStatusCode() < 300) {
            // Log a success!
            if (log.isDebugEnabled()) {
                log.debug(buildResponseMessage("DEBUG", requestWrapper.getMethod(), requestWrapper.getRequestURI(),
                        String.valueOf(respWrapper.getStatus()), content));
            }
        } else if (respWrapper.getStatusCode() >= 300 && respWrapper.getStatusCode() < 400) {
            // Log a warning
            log.warn(buildResponseMessage("WARN", requestWrapper.getMethod(), requestWrapper.getRequestURI(),
                    String.valueOf(respWrapper.getStatus()), content));
        } else if (respWrapper.getStatusCode() >= 400) {
            // Log an error
            log.error(buildResponseMessage("ERROR", requestWrapper.getMethod(), requestWrapper.getRequestURI(),
                    String.valueOf(respWrapper.getStatus()), content));
        } else {
            // This shouldn't happen, but let's log it just in case we get here
            if (log.isDebugEnabled()) {
                log.debug(buildResponseMessage("DEBUG", requestWrapper.getMethod(), requestWrapper.getRequestURI(),
                        String.valueOf(respWrapper.getStatus()), content));
            }
        }

        response.getWriter().write(content);

    }

    private String buildRequestMessage(String method, String uri, Map<String, String> headers,
                                       Map<String, String[]> queryParams, String body) {

        StringBuffer sb = new StringBuffer();
        sb.append("\"severity\":\"DEBUG\",");
        sb.append("\"timestamp\":\"").append(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date())).append("\",");
        sb.append("\"name\":\"").append(method).append(" ").append(uri).append("\",");
        sb.append("\"message\":{");
        sb.append("\"requestContext\":\"").append(headers.get("request-context")).append("\",");
        sb.append("\"requestParams\":");
        int qParams = 0;
        for (String paramName : queryParams.keySet()) {
            if (qParams == 0 && paramName != null) {
                sb.append("{");
            } else {
                sb.append(",");
            }

            sb.append("\"").append(paramName).append("\":\"").append(queryParams.get(paramName)[0]).append("\"");
            qParams += 1;
        }
        if (qParams > 0) {
            sb.append("}");
        }

        sb.append(",\"requestBody\":\"").append(body).append("\",");
        sb.append("\"requestHeaders\":");
        int hCount = 0;
        for (String header : headers.keySet()) {
            if (hCount == 0 && header != null) {
                sb.append("{");
            } else {
                sb.append(",");
            }

            sb.append("\"").append(header).append("\":\"").append(headers.get(header)).append("\"");
            hCount += 1;
        }
        if (hCount > 0) {
            sb.append("}");
        }

        sb.append("}");
        return sb.toString();
    }

    private String buildResponseMessage(String severity, String method, String uri, String statusCode, String body) {

        if (body.isEmpty()) {
            body = "\"\"";
        }

        StringBuffer sb = new StringBuffer();
        sb.append("\"severity\":\"").append(severity).append("\",");
        sb.append("\"timestamp\":\"").append(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date())).append("\",");
        sb.append("\"name\":\"").append(method).append(" ").append(uri).append("\",");
        sb.append("\"message\":{");
        sb.append("\"statusCode\":\"").append(statusCode).append("\",");
        sb.append("\"responseBody\":").append(body);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // NOOP
    }

    @Override
    public void destroy() {
        // NOOP
    }
}
