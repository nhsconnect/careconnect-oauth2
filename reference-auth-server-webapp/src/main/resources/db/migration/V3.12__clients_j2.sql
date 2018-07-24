delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'j2_qrisk_app');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'j2_qrisk_app');
delete from client_details where client_id = 'j2_qrisk_app';

INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method, logo_uri, client_uri,client_description) VALUES
	('j2_qrisk_app', 'J2 Qrisk App',  86400, 'NONE', 'https://avatars2.githubusercontent.com/u/841981?s=200&v=4', 'https://54.201.252.26/csp/qrisk/launch.html','QRisk by J2 Interactive' );

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'j2_qrisk_app'), 'openid'),
	((SELECT id from client_details where client_id = 'j2_qrisk_app'), 'profile'),
	((SELECT id from client_details where client_id = 'j2_qrisk_app'), 'offline_access'),
	((SELECT id from client_details where client_id = 'j2_qrisk_app'), 'launch'),
	((SELECT id from client_details where client_id = 'j2_qrisk_app'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'j2_qrisk_app'), 'user/*.read');


INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'j2_qrisk_app'), 'authorization_code');
