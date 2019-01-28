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
import com.universalinvents.codeservice.pairings.Pairing;
import com.universalinvents.codeservice.studios.Studio;
import com.universalinvents.codeservice.partners.ReferralPartner;
import com.universalinvents.codeservice.retailers.Retailer;
import com.universalinvents.codeservice.utilities.SqlCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

public class RetailerCodeSpecification implements Specification<RetailerCode> {

    private SqlCriteria criteria;

    public RetailerCodeSpecification(SqlCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<RetailerCode> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperand().equalsIgnoreCase(":")) {

            if (criteria.getKey().contains(".studio")) {
                // Handle joins with studio by looking for a '.studio' within the criteria key
                Join<RetailerCode, Content> contentJoin = root.join("content");
                Join<Content, Studio> studioJoin = contentJoin.join("studio");
                return cb.equal(studioJoin.get("id"), criteria.getValue());

            } else if (criteria.getKey().contains(".partner")) {
                // Handle joins with partner by looking for a '.partner' within the criteria key
                Join<RetailerCode, Retailer> retailerJoin = root.join("retailer");
                Join<Retailer, ReferralPartner> partnerJoin = retailerJoin.join("referralPartners");
                return cb.equal(partnerJoin.get("id"), criteria.getValue());

            } else if (criteria.getKey().contains(".master_code")) {
                // Handle joins with master_code by looking for a '.master_code' within the criteria key
                Join<RetailerCode, Pairing> pairingJoin = root.join("pairing");
                Join<Pairing, MasterCode> masterCodeJoin = pairingJoin.join("masterCode");
                return cb.equal(masterCodeJoin.get("code"), criteria.getValue());

            } else {
                return cb.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (criteria.getOperand().equalsIgnoreCase(">")) {
            if (criteria.getValue() instanceof Date) {
                return cb.greaterThan(root.get(criteria.getKey()), (Date) criteria.getValue());
            } else {
                return cb.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperand().equalsIgnoreCase("<")) {
            if (criteria.getValue() instanceof Date) {
                return cb.lessThan(root.get(criteria.getKey()), (Date) criteria.getValue());
            } else {
                return cb.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        }
        return null;
    }
}
