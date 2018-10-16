package com.universalinvents.udccs.studios;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.utilities.SqlCriteria;
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
