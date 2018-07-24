--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;

--
-- Insert client information into the temporary tables. To add clients to the HSQL database, edit things here.
--

INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection, token_endpoint_auth_method, subject_type, logo_uri) VALUES
	('client', 'secret', 'Test Client', false, null, 3600, 600, true, null, null, null),
	('fhir_demo', NULL, 'FHIR Demo App', false, null, 3600, 600, true, 'NONE', 'PUBLIC', 'https://content.hspconsortium.org/images/hspc/icon/HSPCSandboxNoIconApp-210x150.png');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'client'), 'openid'),
	((SELECT id from client_details where client_id = 'client'), 'profile'),
	((SELECT id from client_details where client_id = 'client'), 'smart/orchestrate_launch'),
	((SELECT id from client_details where client_id = 'client'), 'launch'),
	((SELECT id from client_details where client_id = 'client'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'client'), 'launch/encounter'),
	((SELECT id from client_details where client_id = 'client'), 'launch/resource'),
	((SELECT id from client_details where client_id = 'client'), 'launch/other'),
	((SELECT id from client_details where client_id = 'client'), 'user/Patient.read'),
	((SELECT id from client_details where client_id = 'client'), 'user/*.*'),
	((SELECT id from client_details where client_id = 'client'), 'user/*.read'),
	((SELECT id from client_details where client_id = 'client'), 'patient/*.*'),
	((SELECT id from client_details where client_id = 'client'), 'patient/*.read'),
	((SELECT id from client_details where client_id = 'client'), 'offline_access'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'openid'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'profile'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'smart/orchestrate_launch'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'launch'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'launch/encounter'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'launch/resource'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'launch/other'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'user/Patient.read'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'user/*.*'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'user/*.read'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'patient/*.*'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'patient/*.read'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	((SELECT id from client_details where client_id = 'client'), 'http://localhost/'),
	((SELECT id from client_details where client_id = 'client'), 'http://localhost:8080/'),
	((SELECT id from client_details where client_id = 'fhir_demo'), '');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'client'), 'authorization_code'),
	((SELECT id from client_details where client_id = 'client'), 'urn:ietf:params:oauth:grant_type:redelegate'),
	((SELECT id from client_details where client_id = 'client'), 'implicit'),
	((SELECT id from client_details where client_id = 'client'), 'refresh_token'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'implicit'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'refresh_token'),
	((SELECT id from client_details where client_id = 'fhir_demo'), 'authorization_code');

--
-- Close the transaction and turn autocommit back on
--

COMMIT;



