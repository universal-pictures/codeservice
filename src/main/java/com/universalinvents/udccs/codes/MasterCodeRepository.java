package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.apps.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by dsherman on 2/27/17.
 */
public interface MasterCodeRepository extends JpaRepository<MasterCode, String>
{
    List<MasterCode> findByApp(final App app);
//    long countByPairedBy(String pairedBy);
//
//    long countByRedeemedBy(String redeemedBy);
}
