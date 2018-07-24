INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('my_web_app', 'My Web App', 'https://content.hspconsortium.org/images/my-web-app/logo/my.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'my_web_app'), 'openid'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'profile'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'offline_access'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'launch'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'user/*.read'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'user/*.*'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'patient/*.read'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'patient/*.write'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'patient/Patient.read'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'patient/Patient.write'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'patient/Observation.read'),
	((SELECT id from client_details where client_id = 'my_web_app'), 'patient/Observation.write');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'my_web_app'), 'authorization_code');
