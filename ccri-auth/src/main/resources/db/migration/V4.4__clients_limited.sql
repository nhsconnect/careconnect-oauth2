delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'limited-access');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'limited-access');
delete from client_details where client_id = 'limited-access';

INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
  ('limited-access', 'AI8OGCYWjvnj-NY0zaP0H2e6_El_yO2pq43wK4YKk8UnBR_JZ5ivkmkXFtlkiL6LKWsL8H7ksab0V_Hk9c4OeMI', 'OAuth2-LimitedAccess', false, null, 3600, 600, true);

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'limited-access'), 'profile');

-- Grant type

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'limited-access'), 'client_credentials');
