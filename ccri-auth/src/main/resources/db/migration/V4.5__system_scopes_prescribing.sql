--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;

--
-- Insert scope information into the temporary tables.
--

INSERT INTO system_scope (scope, description, icon, default_scope, restricted) VALUES
  ('user/MedicationRequest.read', 'Read all MedicationPrescriptions accessible by the user', 'certificate', false, false),
  ('user/MedicationRequest.write', 'Write all MedicationPrescriptions accessible by the user', 'certificate', false, false),
  ('patient/MedicationRequest.read', 'Read all MedicationPrescriptions for a given patient', 'certificate', false, false),
  ('patient/MedicationRequest.write', 'Write all MedicationPrescriptions for a given patient', 'certificate', false, false);

delete from system_scope where scope = 'user/MedicationOrder.read';
delete from system_scope where scope = 'patient/MedicationOrder.read';
delete from system_scope where scope = 'user/MedicationPrescription.read';
delete from system_scope where scope = 'patient/MedicationPrescription.read';

delete from system_scope where scope = 'user/MedicationOrder.write';
delete from system_scope where scope = 'patient/MedicationOrder.write';
delete from system_scope where scope = 'user/MedicationPrescription.write';
delete from system_scope where scope = 'patient/MedicationPrescription.write';

COMMIT;

