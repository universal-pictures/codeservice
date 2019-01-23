package com.universalinvents.codeservice.partners;

import com.universalinvents.codeservice.retailers.Retailer;
import com.universalinvents.codeservice.studios.Studio;
import com.universalinvents.codeservice.utilities.SqlCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

public class ReferralPartnerSpecification implements Specification<ReferralPartner> {

    private SqlCriteria criteria;

    public ReferralPartnerSpecification(SqlCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ReferralPartner> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperand().equalsIgnoreCase(":")) {
            if (criteria.getKey().equalsIgnoreCase("retailerId")) {
                Join<ReferralPartner, Retailer> retailers = root.join("retailers");
                return cb.equal(retailers.get("id"), criteria.getValue());
            } else if (criteria.getKey().equalsIgnoreCase("retailerIds")) {
                Join<ReferralPartner, Retailer> retailers = root.join("retailers");
                return root.get("retailers").in(criteria.getValue());
            } else if (criteria.getKey().equalsIgnoreCase("studioId")) {
                Join<ReferralPartner, Studio> studios = root.join("studios");
                return cb.equal(studios.get("id"), criteria.getValue());
            } else if (criteria.getKey().equalsIgnoreCase("studioIds")) {
                Join<ReferralPartner, Studio> studios = root.join("studios");
                return root.get("studios").in(criteria.getValue());
            } else {
                return cb.equal(root.get(criteria.getKey()), criteria.getValue());
            }
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
