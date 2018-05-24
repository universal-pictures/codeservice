package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.pairings.Pairing;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.utilities.SqlCriteria;
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
