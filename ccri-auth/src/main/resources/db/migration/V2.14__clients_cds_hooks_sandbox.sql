INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
  ('cds-hooks-sandbox', 'CDS Hooks Sandbox', 'https://content.hspconsortium.org/images/cds-hooks-sandbox/logo/CdsHooks.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'openid'),
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'profile'),
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'online_access'),
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'launch'),
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'launch/patient'),
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'patient/*.*'),
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'user/*.*')
;

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'cds-hooks-sandbox'), 'authorization_code');
