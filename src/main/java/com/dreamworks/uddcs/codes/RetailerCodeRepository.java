package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.retailers.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by dsherman on 2/27/17.
 */
public interface RetailerCodeRepository extends JpaRepository<RetailerCode, Long>
{
    List<RetailerCode> findByPairedOnAndContentAndRetailer(final Date date, final Content content, final Retailer retailer);

    RetailerCode findFirstByPairedOnAndContentAndRetailer(final Date date, final Content content, final Retailer retailer);
}
