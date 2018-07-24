--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;

--
-- Insert scope information into the temporary tables.
--

INSERT INTO system_scope (scope, description, icon, default_scope, restricted) VALUES
  ('user/Bundle.write', 'Write Bundle accessible by the user', 'certificate', false, false),
  ('system/Bundle.write', 'Write Bundle accessible by the system', 'certificate', false, false),
  ('system/DocumentReference.read', 'Read all DocumentReferences accessible by the system', 'certificate', false, false),
  ('system/Binary.read', 'Read all Documents or Images accessible by the system', 'certificate', false, false);


COMMIT;

