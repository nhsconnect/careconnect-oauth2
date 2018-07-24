INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method, client_secret) VALUES
  ('sand_man', 'Sandbox Manager', 86400, 'NONE', null);

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'sand_man'), 'user/*.*'),
  ((SELECT id from client_details where client_id = 'sand_man'), 'smart/orchestrate_launch'),
  ((SELECT id from client_details where client_id = 'sand_man'), 'openid'),
  ((SELECT id from client_details where client_id = 'sand_man'), 'profile');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'sand_man'), 'authorization_code');

INSERT INTO whitelisted_site (creator_user_id, client_id) VALUES
  ('admin', 'sand_man');

INSERT INTO whitelisted_site_scope (owner_id, scope) VALUES
  ((SELECT id from whitelisted_site where client_id = 'sand_man'), 'user/*.*'),
  ((SELECT id from whitelisted_site where client_id = 'sand_man'), 'smart/orchestrate_launch'),
  ((SELECT id from whitelisted_site where client_id = 'sand_man'), 'openid'),
  ((SELECT id from whitelisted_site where client_id = 'sand_man'), 'profile');

INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method, client_secret) VALUES
  ('sandman_admin', 'Sandbox Manager Admin', 86400, 'SECRET_BASIC', 'secret');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'sandman_admin'), 'client_credentials');

INSERT INTO client_authority (owner_id, authority) VALUES
  ((SELECT id from client_details where client_id = 'sandman_admin'), 'ROLE_ADMIN'),
  ((SELECT id from client_details where client_id = 'sandman_admin'), 'ROLE_USER');
