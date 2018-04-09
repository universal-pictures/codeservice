package com.universalinvents.udccs.contents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by dsherman on 2/27/17.
 */
public interface ContentRepository extends JpaRepository<Content, Long>, JpaSpecificationExecutor<Content> {}
