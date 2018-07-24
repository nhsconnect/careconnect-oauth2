INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
  ('nhs-smart-ehr', 'APa5oCe6SHhty_or2q34WpNcq0-X957n6p48TkAJw14YCtmZeQil60XvCfuByIPd8DlXyusxAGxp5_Z5UKlgZJU', 'NHS SMART on FHIR EHR', false, null, 3600, 600, true);

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'nhs-smart-ehr'), 'user/Patient.read'),
  ((SELECT id from client_details where client_id = 'nhs-smart-ehr'), 'user/DocumentReference.read'),
  ((SELECT id from client_details where client_id = 'nhs-smart-ehr'), 'user/Binary.read'),
  ((SELECT id from client_details where client_id = 'nhs-smart-ehr'), 'smart/orchestrate_launch');


INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'nhs-smart-ehr'), 'user/Bundle.read');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'nhs-smart-ehr'), 'authorization_code');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'nhs-smart-ehr'), 'user/*.read');


