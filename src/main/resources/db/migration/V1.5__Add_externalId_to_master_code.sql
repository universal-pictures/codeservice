ALTER TABLE udccs.master_code ADD COLUMN external_id VARCHAR(255);
CREATE INDEX IDX_mc_external_id ON udccs.master_code (external_id);