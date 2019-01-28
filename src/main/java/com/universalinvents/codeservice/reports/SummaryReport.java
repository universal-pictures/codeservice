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

public class SummaryReport {
    private long studios;
    private long partners;
    private long retailers;
    private long contents;

    public SummaryReport() {}

    public SummaryReport(long studios, long partners, long retailers, long contents) {
        this.studios = studios;
        this.partners = partners;
        this.retailers = retailers;
        this.contents = contents;
    }

    public long getStudios() {
        return studios;
    }

    public void setStudios(long studios) {
        this.studios = studios;
    }

    public long getPartners() {
        return partners;
    }

    public void setPartners(long partners) {
        this.partners = partners;
    }

    public long getRetailers() {
        return retailers;
    }

    public void setRetailers(long retailers) {
        this.retailers = retailers;
    }

    public long getContents() {
        return contents;
    }

    public void setContents(long contents) {
        this.contents = contents;
    }
}
