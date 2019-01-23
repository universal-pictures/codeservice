package com.universalinvents.codeservice.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

public class EventWrapper {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date timestamp;
    private String object;
    private String event;
    private String eventId;
    private AbstractEvent data;
    private String traceId;
    private String schemaVersion;

    public EventWrapper(String object, String event, AbstractEvent data, String traceId, String schemaVersion) {
        this.object = object;
        this.timestamp = new Date();
        this.event = event;
        this.data = data;
        this.traceId = traceId;
        this.eventId = UUID.randomUUID().toString();
        this.schemaVersion = schemaVersion;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getObject() {
        return object;
    }

    public String getEvent() {
        return event;
    }

    public String getEventId() {
        return eventId;
    }

    public AbstractEvent getData() {
        return data;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

}
