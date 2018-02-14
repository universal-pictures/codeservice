package com.universalinvents.udccs.codes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by dsherman on 2/27/17.
 */
public interface RetailerCodeRepository extends JpaRepository<RetailerCode, String>, JpaSpecificationExecutor<RetailerCode>
{
}
