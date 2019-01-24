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

package com.universalinvents.codeservice.external;

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
