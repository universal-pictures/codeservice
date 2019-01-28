/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
