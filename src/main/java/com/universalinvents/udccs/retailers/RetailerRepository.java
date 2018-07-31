package com.universalinvents.udccs.retailers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RetailerRepository extends JpaRepository<Retailer, Long>, JpaSpecificationExecutor<Retailer> {
}
