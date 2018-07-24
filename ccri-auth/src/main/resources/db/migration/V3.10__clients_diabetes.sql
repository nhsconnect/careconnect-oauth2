INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('diabetes', 'Diabetes',  86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'diabetes'), 'openid'),
	((SELECT id from client_details where client_id = 'diabetes'), 'profile'),
	((SELECT id from client_details where client_id = 'diabetes'), 'offline_access'),
	((SELECT id from client_details where client_id = 'diabetes'), 'launch'),
	((SELECT id from client_details where client_id = 'diabetes'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'diabetes'), 'user/*.read');


INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'diabetes'), 'authorization_code');
