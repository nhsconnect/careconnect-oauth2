INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method) VALUES
  ('patient_picker', 'Patient Picker', 86400, 'NONE');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
  ((SELECT id from client_details where client_id = 'patient_picker'), '{{patient_picker_server_external_url}}');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'patient_picker'), 'user/*.*'),
  ((SELECT id from client_details where client_id = 'patient_picker'), 'smart/orchestrate_launch'),
  ((SELECT id from client_details where client_id = 'patient_picker'), 'openid'),
  ((SELECT id from client_details where client_id = 'patient_picker'), 'profile');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'patient_picker'), 'authorization_code');

INSERT INTO whitelisted_site (creator_user_id, client_id) VALUES
  ('admin', 'patient_picker');

INSERT INTO whitelisted_site_scope (owner_id, scope) VALUES
  ((SELECT id from whitelisted_site where client_id = 'patient_picker'), 'user/*.*');

INSERT INTO whitelisted_site_scope (owner_id, scope) VALUES
  ((SELECT id from whitelisted_site where client_id = 'patient_picker'), 'smart/orchestrate_launch');

INSERT INTO whitelisted_site_scope (owner_id, scope) VALUES
  ((SELECT id from whitelisted_site where client_id = 'patient_picker'), 'openid');

INSERT INTO whitelisted_site_scope (owner_id, scope) VALUES
  ((SELECT id from whitelisted_site where client_id = 'patient_picker'), 'profile');
