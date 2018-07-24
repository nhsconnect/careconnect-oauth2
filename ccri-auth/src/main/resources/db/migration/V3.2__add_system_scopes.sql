--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;

--
-- Insert scope information into the temporary tables.
--

INSERT INTO system_scope (scope, description, icon, default_scope, restricted) VALUES
  ('user/DocumentReference.read', 'Read all DocumentReferences accessible by the user', 'certificate', false, false),
  ('user/Binary.read', 'Read all Documents or Images accessible by the user', 'certificate', false, false);

COMMIT;

