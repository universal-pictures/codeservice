package com.universalinvents.udccs.studios;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by kkirkland on 11/8/17.
 */
public interface StudioRepository extends JpaRepository<Studio, Long> {
    List<Studio> findByName(final String name);
}
