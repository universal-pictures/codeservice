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

package com.universalinvents.codeservice.reports;

import java.util.List;

public class RetailerStudioDetails {

    private String name;
    private RetailerCodeDetails retailerCodes;
    private List<RetailerContentDetails> content;

    public RetailerStudioDetails() {}

    public RetailerStudioDetails(String name, RetailerCodeDetails retailerCodes, List<RetailerContentDetails> content) {
        this.name = name;
        this.retailerCodes = retailerCodes;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RetailerCodeDetails getRetailerCodes() {
        return retailerCodes;
    }

    public void setRetailerCodes(RetailerCodeDetails retailerCodes) {
        this.retailerCodes = retailerCodes;
    }

    public List<RetailerContentDetails> getContent() {
        return content;
    }

    public void setContent(List<RetailerContentDetails> content) {
        this.content = content;
    }
}
