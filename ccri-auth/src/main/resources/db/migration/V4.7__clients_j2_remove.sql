delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'j2_qrisk_app');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'j2_qrisk_app');
delete from client_details where client_id = 'j2_qrisk_app';

