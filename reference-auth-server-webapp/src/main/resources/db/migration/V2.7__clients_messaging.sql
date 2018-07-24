INSERT INTO client_details (client_id, client_name, jwks_uri, token_endpoint_auth_method, access_token_validity_seconds) VALUES
	('messaging_client', 'Reference Messaging', '{{auth_server_external_url}}/jwk', 'PRIVATE_KEY', 86400);

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'messaging_client'), 'system/*.read'),
	((SELECT id from client_details where client_id = 'messaging_client'), 'system/*.write'),
	((SELECT id from client_details where client_id = 'messaging_client'), 'offline_access');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'messaging_client'), 'client_credentials'),
	((SELECT id from client_details where client_id = 'messaging_client'), 'refresh_token');
