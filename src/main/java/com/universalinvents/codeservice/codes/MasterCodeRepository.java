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

import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.apps.App;
import com.universalinvents.codeservice.partners.ReferralPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by dsherman on 2/27/17.
 */
public interface MasterCodeRepository extends JpaRepository<MasterCode, String>, JpaSpecificationExecutor<MasterCode> {
//    List<MasterCode> findByApp(final App app);
//    List<MasterCode> findByContent(final Content content);
//    List<MasterCode> findByReferralPartner(final ReferralPartner referralPartner);
//    List<MasterCode> findByStatus(final MasterCode.Status status);

    List<MasterCode> findByContent(Content content);

    long countByStatus(MasterCode.Status status);

    long countByApp(App app);

    long countByAppAndStatus(App app, MasterCode.Status status);

    long countByContentInAndStatus(List<Content> content, MasterCode.Status status);

    long countByContentAndStatus(Content content, MasterCode.Status status);

    long countByContentAndReferralPartnerAndStatus(Content content, ReferralPartner partner, MasterCode.Status status);

    long countByContentAndReferralPartnerAndAppAndStatus(Content content, ReferralPartner partner, App app,
                                                         MasterCode.Status status);
//    long countByRedeemedBy(String redeemedBy);
}
