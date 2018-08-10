--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;

--
-- Insert scope information into the temporary tables.
--

INSERT INTO system_scope (scope, description, icon, default_scope, restricted) VALUES
  ('system/*.read', 'Read all accessible by the system', 'certificate', false, false),
  ('system/*.write', 'Write all accessible by the system', 'certificate', false, false),
  ('user/Bundle.read', 'Read all Bundle for a given user', 'certificate', false, false),
  ('patient/Bundle.write', 'Write all Bundle for a given patient', 'certificate', false, false);



COMMIT;

