package com.universalinvents.udccs.partners;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by dsherman on 2/27/17.
 */
public interface ReferralPartnerRepository extends JpaRepository<ReferralPartner, Long> {
    List<ReferralPartner> findByName(final String name);
}
