package com.universalinvents.udccs.retailers;

import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.utilities.SqlCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

public class RetailerSpecification implements Specification<Retailer> {

    private SqlCriteria criteria;

    public RetailerSpecification(SqlCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Retailer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperand().equalsIgnoreCase(":")) {
            if (criteria.getKey().equalsIgnoreCase("partnerId")) {
                Join<Retailer, ReferralPartner> partners = root.join("referralPartners");
                return cb.equal(partners.get("id"), criteria.getValue());
            } else if (criteria.getKey().equalsIgnoreCase("partnerIds")) {
                Join<Retailer, ReferralPartner> partners = root.join("referralPartners");
                return root.get("referralPartners").in(criteria.getValue());
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
