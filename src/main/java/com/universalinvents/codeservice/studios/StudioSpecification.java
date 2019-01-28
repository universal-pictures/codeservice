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

package com.universalinvents.codeservice.studios;

import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.utilities.SqlCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class StudioSpecification implements Specification<Studio> {

    private SqlCriteria criteria;

    public StudioSpecification(SqlCriteria criteria) { this.criteria = criteria; }

    @Override
    public Predicate toPredicate(Root<Studio> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperand().equalsIgnoreCase(":")) {
            if (criteria.getKey().equalsIgnoreCase("contentId")) {
                Join<Studio, Content> contents = root.join("contents");
                return cb.equal(contents.get("id"), criteria.getValue());
            } else if (criteria.getKey().equalsIgnoreCase("contentIds")) {
                Join<Studio, Content> contents = root.join("contents");
                return root.get("contents").in(criteria.getValue());
            } else {
                return cb.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
