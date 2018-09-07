delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'clinical-access');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'clinical-access');
delete from client_details where client_id = 'clinical-access';

INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
  ('clinical-access', 'QOm0VcqJqa9stA1R0MJzHjCN_uYdo0PkY8OT68UCk2XDFxFrAUjajuqOvIom5dISjKshx2YiU51mXtx7W5UOwQ', 'OAuth2-ClinicalAccess', false, null, 3600, 600, true);

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'profile');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'user/MedicationStatement.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'patient/MedicationStatement.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'patient/Immunization.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'user/Immunization.read');

  INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'user/Patient.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'patient/Patient.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'patient/Condition.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'user/Condition.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'user/MedicationRequest.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'patient/MedicationRequest.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'user/Encounter.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'patient/Encounter.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'user/Observation.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'patient/Observation.read');

-- Grant type

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'clinical-access'), 'client_credentials');

