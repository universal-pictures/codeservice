package com.universalinvents.udccs.codes;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class RetailerCodeSpecification implements Specification<RetailerCode> {

    private CodeCriteria criteria;

    public RetailerCodeSpecification(CodeCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<RetailerCode> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperand().equalsIgnoreCase(":")) {
            return cb.equal(root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperand().equalsIgnoreCase(">")) {
            if (criteria.getValue() instanceof Date) {
                return cb.greaterThan(root.get(criteria.getKey()), (Date)criteria.getValue());
            } else {
                return cb.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperand().equalsIgnoreCase("<")) {
            if (criteria.getValue() instanceof Date) {
                return cb.lessThan(root.get(criteria.getKey()), (Date)criteria.getValue());
            } else {
                return cb.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        }
        return null;
    }
}
