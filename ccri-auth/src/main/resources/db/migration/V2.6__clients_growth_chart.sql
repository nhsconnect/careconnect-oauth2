INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('growth_chart', 'Growth Chart', 'https://content.hspconsortium.org/images/growth-chart/logo/growth-chart.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'growth_chart'), 'openid'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'profile'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'offline_access'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'launch'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'user/*.read'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'user/*.*'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'patient/*.read'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'patient/*.write'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'patient/Patient.read'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'patient/Patient.write'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'patient/Observation.read'),
	((SELECT id from client_details where client_id = 'growth_chart'), 'patient/Observation.write');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'growth_chart'), 'authorization_code');
