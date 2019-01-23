package com.universalinvents.codeservice.partners;

import com.universalinvents.codeservice.codes.MasterCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface ReferralPartnerRepository extends JpaRepository<ReferralPartner, Long>, JpaSpecificationExecutor<ReferralPartner> {
//    @Query(value = "SELECT p FROM ReferralPartner p JOIN p.codes c WHERE c.content ")
    List<ReferralPartner> findDistinctByCodesIn(List<MasterCode> masterCodes);
}
