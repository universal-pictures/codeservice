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
import com.universalinvents.codeservice.utilities.SqlCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

public class MasterCodeSpecification implements Specification<MasterCode> {

    private SqlCriteria criteria;

    public MasterCodeSpecification(SqlCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<MasterCode> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperand().equalsIgnoreCase(":")) {
            // Handle joins with studio by looking for a '.studio' within the criteria key
            if (criteria.getKey().contains(".studio")) {
                Join<MasterCode, Content> contentJoin = root.join("content");
                Join<Content, Studio> studioJoin = contentJoin.join("studio");
                return cb.equal(studioJoin.get("id"), criteria.getValue());

            } else if (criteria.getKey().contains(".retailer_code")) {
                // Handle joins with retailer_code by looking for a '.retailer_code' within the criteria key
                Join<MasterCode, Pairing> pairingJoin = root.join("pairing");
                Join<Pairing, RetailerCode> retailerCodeJoin = pairingJoin.join("retailerCode");
                return cb.equal(retailerCodeJoin.get("code"), criteria.getValue());

            } else {
                return cb.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (criteria.getOperand().equalsIgnoreCase(">")) {
            if (criteria.getValue() instanceof java.util.Date) {
                return cb.greaterThan(root.get(criteria.getKey()), (Date)criteria.getValue());
            } else {
                return cb.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperand().equalsIgnoreCase("<")) {
            if (criteria.getValue() instanceof java.util.Date) {
                return cb.lessThan(root.get(criteria.getKey()), (Date)criteria.getValue());
            } else {
                return cb.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        }
        return null;
    }
}
