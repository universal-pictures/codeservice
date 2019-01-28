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

import com.universalinvents.codeservice.codes.MasterCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface ReferralPartnerRepository extends JpaRepository<ReferralPartner, Long>, JpaSpecificationExecutor<ReferralPartner> {
//    @Query(value = "SELECT p FROM ReferralPartner p JOIN p.codes c WHERE c.content ")
    List<ReferralPartner> findDistinctByCodesIn(List<MasterCode> masterCodes);
}
