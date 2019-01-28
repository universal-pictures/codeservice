/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.universalinvents.codeservice.codes;

import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.retailers.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RetailerCodeRepository extends JpaRepository<RetailerCode, String>,
        JpaSpecificationExecutor<RetailerCode> {
    @Query(value = "SELECT DISTINCT format FROM RetailerCode WHERE content = :content AND retailer = :retailer")
    List<String> findDistinctFormatsByContentAndRetailer(@Param("content") Content content,
                                                         @Param("retailer") Retailer retailer);

    @Query(value = "SELECT DISTINCT retailer FROM RetailerCode WHERE content_id = :contentId")
    List<Retailer> findDistinctRetailersByContentId(@Param("contentId") Long contentId);

    long countByStatus(RetailerCode.Status status);

    long countByContentInAndStatus(List<Content> content, RetailerCode.Status status);

    long countByContentAndStatus(Content content, RetailerCode.Status status);

    long countByContentAndRetailerAndStatus(Content content, Retailer retailer, RetailerCode.Status status);

    long countByContentAndRetailerAndFormatAndStatus(Content content, Retailer retailer, String format,
                                                     RetailerCode.Status status);
}
