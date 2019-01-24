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

package com.universalinvents.codeservice.pairings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("PairMasterCodeRequest")
public class PairMasterCodeRequest
{
    @ApiModelProperty(value = "The id of the Retailer from which to find a code to pair with a Master Code",
                      example = "1",
                      required = true)
    private Long retailerId;
    @ApiModelProperty(value = "Who's executing this request? Value can be any string but an external customer " +
                              "id is preferred.",
                      example = "qas8fcw00sddergwpjkn4weiibnc925z",
                      required = false)
    private String pairedBy;

    public PairMasterCodeRequest() {}

    public PairMasterCodeRequest(Long retailerId, String pairedBy)
    {
        this.retailerId = retailerId;
        this.pairedBy = pairedBy;
    }

    public String getPairedBy()
    {
        return pairedBy;
    }

    public void setPairedBy(String pairedBy)
    {
        this.pairedBy = pairedBy;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }
}
