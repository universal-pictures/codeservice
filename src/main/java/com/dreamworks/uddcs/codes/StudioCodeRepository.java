package com.dreamworks.uddcs.codes;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dsherman on 2/27/17.
 */
public interface StudioCodeRepository extends JpaRepository<StudioCode, String>
{
    long countByPairedBy(String pairedBy);

    long countByRedeemedBy(String redeemedBy);
}
