package com.universalinvents.udccs.retailers;

import com.universalinvents.udccs.contents.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface RetailerRepository extends JpaRepository<Retailer, Long>, JpaSpecificationExecutor<Retailer> {
    List<Retailer> findByContentsIn(List<Content> content);
}
