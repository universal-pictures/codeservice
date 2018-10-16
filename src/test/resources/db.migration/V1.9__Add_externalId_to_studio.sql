ALTER TABLE studio ADD COLUMN external_id VARCHAR(255);
CREATE INDEX IDX_s_external_id ON studio (external_id);