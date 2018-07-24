INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('child_measurements', 'Child Measurements',  86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'child_measurements'), 'openid'),
	((SELECT id from client_details where client_id = 'child_measurements'), 'profile'),
	((SELECT id from client_details where client_id = 'child_measurements'), 'offline_access'),
	((SELECT id from client_details where client_id = 'child_measurements'), 'launch'),
	((SELECT id from client_details where client_id = 'child_measurements'), 'launch/patient'),
	((SELECT id from client_details where client_id = 'child_measurements'), 'user/*.read');


INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'child_measurements'), 'authorization_code');

UPDATE user_info SET email = 'devmikey.js@gmail.com' WHERE
  sub = '01922.FLANRJQW';