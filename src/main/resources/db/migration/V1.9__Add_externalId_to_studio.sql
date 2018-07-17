ALTER TABLE udccs.studio ADD COLUMN external_id VARCHAR(255);
CREATE INDEX IDX_mc_external_id ON udccs.studio (external_id);