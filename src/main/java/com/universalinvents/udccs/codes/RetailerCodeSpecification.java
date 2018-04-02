package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.utilities.SqlCriteria;
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
