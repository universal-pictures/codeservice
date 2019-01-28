ALTER TABLE udccs.retailer ADD COLUMN generate_codes BOOLEAN NOT NULL DEFAULT 0;
ALTER TABLE udccs.retailer ADD COLUMN logo_url VARCHAR(255);
ALTER TABLE udccs.retailer ADD COLUMN redemption_url VARCHAR(255);