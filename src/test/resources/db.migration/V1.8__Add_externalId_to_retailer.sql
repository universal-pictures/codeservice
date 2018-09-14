ALTER TABLE retailer ADD COLUMN external_id VARCHAR(255);
CREATE INDEX IDX_r_external_id ON retailer (external_id);