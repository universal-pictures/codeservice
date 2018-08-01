package com.universalinvents.udccs.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Value("${event.schema-version}")
    private String schemaVersion;

    public String getSchemaVersion() { return schemaVersion; }
}
