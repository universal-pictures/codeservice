package com.universalinvents.udccs.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.GeneratedValue;

import java.util.Date;

public class EventWrapper {

    private Date timestamp;
    private String event;

    @GeneratedValue(generator = "uuid2")
    private String eventId;

    private AbstractEvent data;

    public EventWrapper(String event, AbstractEvent data) {
        this.timestamp = new Date();
        this.event = event;
        this.data = data;
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

}
