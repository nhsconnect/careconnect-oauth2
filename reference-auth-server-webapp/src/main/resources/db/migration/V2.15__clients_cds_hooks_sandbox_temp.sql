-- temporary registration

INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
  ('48163c5e-88b5-4cb3-92d3-23b800caa927', 'CDS Hooks Sandbox', 'https://content.hspconsortium.org/images/cds-hooks-sandbox/logo/CdsHooks.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'openid'),
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'profile'),
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'online_access'),
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'launch'),
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'launch/patient'),
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'patient/*.*'),
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'user/*.*')
;

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927'), 'authorization_code');
