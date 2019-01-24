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

public class ExternalRetailerCodeRequest {

    private String eidr;
    private String code;

    public ExternalRetailerCodeRequest() {}

    public ExternalRetailerCodeRequest(String eidr, String code) {
        this.eidr = eidr;
        this.code = code;
    }

    public String getEidr() { return eidr; }

    public void setEidr(String eidr) { this.eidr = eidr; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }
}
