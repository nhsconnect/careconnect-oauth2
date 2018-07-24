INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method, client_secret) VALUES
  ('smart_proxy_admin', 'SMART Proxy Admin', 86400, 'SECRET_BASIC', 'secret');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'smart_proxy_admin'), 'client_credentials');

INSERT INTO client_authority (owner_id, authority) VALUES
  ((SELECT id from client_details where client_id = 'smart_proxy_admin'), 'ROLE_ADMIN'),
  ((SELECT id from client_details where client_id = 'smart_proxy_admin'), 'ROLE_USER');
