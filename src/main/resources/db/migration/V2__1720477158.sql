ALTER TABLE CARDS
    ALTER COLUMN CARD_NUMBER TYPE VARCHAR(255) USING (CARD_NUMBER::VARCHAR(255));