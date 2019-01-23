package com.universalinvents.codeservice.codes;

import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.apps.App;
import com.universalinvents.codeservice.partners.ReferralPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by dsherman on 2/27/17.
 */
public interface MasterCodeRepository extends JpaRepository<MasterCode, String>, JpaSpecificationExecutor<MasterCode> {
//    List<MasterCode> findByApp(final App app);
//    List<MasterCode> findByContent(final Content content);
//    List<MasterCode> findByReferralPartner(final ReferralPartner referralPartner);
//    List<MasterCode> findByStatus(final MasterCode.Status status);

    List<MasterCode> findByContent(Content content);

    long countByStatus(MasterCode.Status status);

    long countByApp(App app);

    long countByAppAndStatus(App app, MasterCode.Status status);

    long countByContentInAndStatus(List<Content> content, MasterCode.Status status);

    long countByContentAndStatus(Content content, MasterCode.Status status);

    long countByContentAndReferralPartnerAndStatus(Content content, ReferralPartner partner, MasterCode.Status status);

    long countByContentAndReferralPartnerAndAppAndStatus(Content content, ReferralPartner partner, App app,
                                                         MasterCode.Status status);
//    long countByRedeemedBy(String redeemedBy);
}
