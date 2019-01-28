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

package com.universalinvents.codeservice.partners;

import java.util.Set;

public interface ReferralPartnerRequest {

    public String getName();
    public void setName(String name);

    public String getDescription();
    public void setDescription(String description);

    public String getContactName();
    public void setContactName(String contactName);

    public String getContactEmail();
    public void setContactEmail(String contactEmail);

    public String getContactPhone();
    public void setContactPhone(String contactPhone);

    public Set<Long> getRetailerIds();
    public void setRetailerIds(Set<Long> retailerIds);

    public Set<Long> getStudioIds();
    public void setStudioIds(Set<Long> studioIds);

    public String getStatus();
    public void setStatus(String status);
}
