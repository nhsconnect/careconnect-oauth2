INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('cardiac_risk', 'Cardiac Risk', 'https://content.hspconsortium.org/images/cardiac-risk/logo/cardiac-risk.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'openid'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'profile'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'offline_access'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'launch'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'user/*.read'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'user/*.*'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'patient/*.read'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'patient/*.write'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'patient/Patient.read'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'patient/Patient.write'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'patient/Observation.read'),
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'patient/Observation.write');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'cardiac_risk'), 'authorization_code');
