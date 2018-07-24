INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('bp_centiles', 'BP Centiles', 'https://content.hspconsortium.org/images/bp-centiles/logo/bpc.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'bp_centiles'), 'openid'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'profile'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'offline_access'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'launch'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'user/*.read'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'user/*.*'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'patient/*.read'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'patient/*.write'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'patient/Patient.read'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'patient/Patient.write'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'patient/Observation.read'),
	((SELECT id from client_details where client_id = 'bp_centiles'), 'patient/Observation.write');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'bp_centiles'), 'authorization_code');
