package com.dreamworks.udccs.apps;

import com.dreamworks.udccs.partners.ReferralPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by kkirkland on 11/7/17.
 */
public interface AppRepository extends JpaRepository<App, Long> {
    List<App> findByReferralPartner(final ReferralPartner referralPartner);
}
