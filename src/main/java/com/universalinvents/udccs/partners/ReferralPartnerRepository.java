package com.universalinvents.udccs.partners;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ReferralPartnerRepository extends JpaRepository<ReferralPartner, Long>, JpaSpecificationExecutor<ReferralPartner> {
}
