DELETE FROM retailer_code WHERE status = 'UNALLOCATED';
ALTER TABLE retailer_code MODIFY COLUMN `status` enum('PAIRED','REDEEMED') NOT NULL;