ALTER TABLE retailer_code ADD COLUMN expires_on datetime DEFAULT NULL;
ALTER TABLE retailer_code MODIFY COLUMN `status` enum('PAIRED','REDEEMED','EXPIRED') NOT NULL;