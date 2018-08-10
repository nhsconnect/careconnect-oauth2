delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'medication-access');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'medication-access');
delete from client_details where client_id = 'medication-access';

INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
  ('medication-access', 'IShTVi8mRSV7bVREuU1freiDo79y_8fLX3BBw2nf2eIpv9A_r91VlVuF2LOiK_zLZAkBQCusEXLp_o6DEIgvaQ', 'OAuth2-PatientAccess', false, null, 3600, 600, true);

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'medication-access'), 'user/Patient.read');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'medication-access'), 'user/Bundle.read');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'medication-access'), 'client_credentials');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'medication-access'), 'user/*.read');
