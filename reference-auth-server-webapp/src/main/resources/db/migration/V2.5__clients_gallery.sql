INSERT INTO client_details (client_id, client_name, access_token_validity_seconds, token_endpoint_auth_method) VALUES
	('hspc_gallery', 'HSPC Gallery', 86400, 'NONE');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	((SELECT id from client_details where client_id = 'hspc_gallery'), '{{gallery_server_external_url}}'),
	((SELECT id from client_details where client_id = 'hspc_gallery'), 'http://localhost:{{gallery_server_internal_port}}')
;

INSERT INTO client_scope (owner_id, scope) VALUES
	((SELECT id from client_details where client_id = 'hspc_gallery'), 'user/*.*'),
	((SELECT id from client_details where client_id = 'hspc_gallery'), 'smart/orchestrate_launch'),
	((SELECT id from client_details where client_id = 'hspc_gallery'), 'openid'),
	((SELECT id from client_details where client_id = 'hspc_gallery'), 'profile');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	((SELECT id from client_details where client_id = 'hspc_gallery'), 'authorization_code');

INSERT INTO whitelisted_site (creator_user_id, client_id) VALUES
	('admin', 'hspc_gallery');

INSERT INTO whitelisted_site_scope (owner_id, scope) VALUES
	((SELECT id from whitelisted_site where client_id = 'hspc_gallery'), 'user/*.*'),
	((SELECT id from whitelisted_site where client_id = 'hspc_gallery'), 'smart/orchestrate_launch'),
	((SELECT id from whitelisted_site where client_id = 'hspc_gallery'), 'openid'),
	((SELECT id from whitelisted_site where client_id = 'hspc_gallery'), 'profile')
;
