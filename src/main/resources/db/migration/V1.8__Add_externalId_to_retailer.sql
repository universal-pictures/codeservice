ALTER TABLE udccs.retailer ADD COLUMN external_id VARCHAR(255);
CREATE INDEX IDX_mc_external_id ON udccs.retailer (external_id);