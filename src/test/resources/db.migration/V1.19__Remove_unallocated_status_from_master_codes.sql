DELETE FROM master_code WHERE status = 'UNALLOCATED';
ALTER TABLE master_code MODIFY COLUMN `status` enum('ISSUED', 'PAIRED', 'REDEEMED', 'EXPIRED') NOT NULL;