package com.universalinvents.udccs.apps;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kkirkland on 11/7/17.
 */
public interface AppRepository extends JpaRepository<App, Long> {
}
