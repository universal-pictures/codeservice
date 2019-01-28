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

public class AppDetails {

    private String name;
    private MasterCodeDetails masterCodes;

    public AppDetails() {}

    public AppDetails(String name, MasterCodeDetails masterCodes) {
        this.name = name;
        this.masterCodes = masterCodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MasterCodeDetails getMasterCodes() {
        return masterCodes;
    }

    public void setMasterCodes(MasterCodeDetails masterCodes) {
        this.masterCodes = masterCodes;
    }
}
