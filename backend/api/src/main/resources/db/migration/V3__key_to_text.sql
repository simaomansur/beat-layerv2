ALTER TABLE jams
  ALTER COLUMN "key" TYPE text
  USING "key"::text;
