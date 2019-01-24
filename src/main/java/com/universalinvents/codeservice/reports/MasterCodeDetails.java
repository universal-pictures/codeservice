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

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterCodeDetails {

    @JsonProperty("ISSUED")
    private long issued;
    @JsonProperty("PAIRED")
    private long paired;
    @JsonProperty("REDEEMED")
    private long redeemed;
    @JsonProperty("EXPIRED")
    private long expired;

    public MasterCodeDetails() {}

    public MasterCodeDetails(long issued, long paired, long redeemed, long expired) {
        this.issued = issued;
        this.paired = paired;
        this.redeemed = redeemed;
        this.expired = expired;
    }

    public long getIssued() {
        return issued;
    }

    public void setIssued(long issued) {
        this.issued = issued;
    }

    public long getPaired() {
        return paired;
    }

    public void setPaired(long paired) {
        this.paired = paired;
    }

    public long getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(long redeemed) {
        this.redeemed = redeemed;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }
}
