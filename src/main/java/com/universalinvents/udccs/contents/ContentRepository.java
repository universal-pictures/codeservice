package com.universalinvents.udccs.contents;

import com.universalinvents.udccs.studios.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long>, JpaSpecificationExecutor<Content> {
    List<Content> findByStudio(Studio studio);
}
