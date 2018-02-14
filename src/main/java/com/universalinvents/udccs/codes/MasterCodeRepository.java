package com.universalinvents.udccs.codes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by dsherman on 2/27/17.
 */
public interface MasterCodeRepository extends JpaRepository<MasterCode, String>, JpaSpecificationExecutor<MasterCode>
{
//    List<MasterCode> findByApp(final App app);
//    List<MasterCode> findByContent(final Content content);
//    List<MasterCode> findByReferralPartner(final ReferralPartner referralPartner);
//    List<MasterCode> findByStatus(final MasterCode.Status status);

//    long countByPairedBy(String pairedBy);
//
//    long countByRedeemedBy(String redeemedBy);
}
