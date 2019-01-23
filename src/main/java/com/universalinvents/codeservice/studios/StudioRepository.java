package com.universalinvents.codeservice.studios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by kkirkland on 11/8/17.
 */
public interface StudioRepository extends JpaRepository<Studio, Long>, JpaSpecificationExecutor<Studio> {}
