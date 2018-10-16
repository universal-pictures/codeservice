DELETE FROM udccs.retailer_code WHERE status = 'UNALLOCATED';
ALTER TABLE udccs.retailer_code MODIFY COLUMN `status` enum('PAIRED','REDEEMED') NOT NULL;