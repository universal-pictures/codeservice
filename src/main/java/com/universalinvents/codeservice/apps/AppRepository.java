package com.universalinvents.codeservice.apps;

import com.universalinvents.codeservice.partners.ReferralPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by kkirkland on 11/7/17.
 */
public interface AppRepository extends JpaRepository<App, Long> {
    List<App> findByReferralPartner(ReferralPartner partner);
}
