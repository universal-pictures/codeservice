package com.dreamworks.uddcs.codes;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dsherman on 2/27/17.
 */
public interface MasterCodeRepository extends JpaRepository<MasterCode, String>
{
//    long countByPairedBy(String pairedBy);
//
//    long countByRedeemedBy(String redeemedBy);
}
