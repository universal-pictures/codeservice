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

package com.universalinvents.codeservice.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class RedeemMasterCodeRequest
{
    private String redeemedBy;

    public RedeemMasterCodeRequest() {}

    public RedeemMasterCodeRequest(String redeemedBy)
    {
        this.redeemedBy = redeemedBy;
    }

    public String getRedeemedBy()
    {
        return redeemedBy;
    }

    public void setRedeemedBy(String redeemedBy)
    {
        this.redeemedBy = redeemedBy;
    }
}
