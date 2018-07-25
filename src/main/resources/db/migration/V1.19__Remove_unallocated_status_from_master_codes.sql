DELETE FROM udccs.master_code WHERE status = 'UNALLOCATED';
ALTER TABLE udccs.master_code MODIFY COLUMN `status` enum('ISSUED', 'PAIRED', 'REDEEMED', 'EXPIRED') NOT NULL;