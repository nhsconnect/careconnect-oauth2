
delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'client');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'client');
delete from client_details where client_id = 'client';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'fhir_demo');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'fhir_demo');
delete from client_details where client_id = 'fhir_demo';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'hspc_appointments');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'hspc_appointments');
delete from client_details where client_id = 'hspc_appointments';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'bilirubin_chart');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'bilirubin_chart');
delete from client_details where client_id = 'bilirubin_chart';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'patient_data_manager');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'patient_data_manager');
delete from client_details where client_id = 'patient_data_manager';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'hspc_resource_server');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'hspc_resource_server');
delete from client_details where client_id = 'hspc_resource_server';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'cds-hooks-sandbox');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'cds-hooks-sandbox');
delete from client_details where client_id = 'cds-hooks-sandbox';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927');
delete from client_details where client_id = '48163c5e-88b5-4cb3-92d3-23b800caa927';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'sandman_admin');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'sandman_admin');
delete from client_details where client_id = 'sandman_admin';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'sandman');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'sandman');
delete from client_details where client_id = 'sandman';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'smart_proxy_admin');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'smart_proxy_admin');
delete from client_details where client_id = 'smart_proxy_admin';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'patient_picker');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'patient_picker');
delete from client_details where client_id = 'patient_picker';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'hspc_gallery');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'hspc_gallery');
delete from client_details where client_id = 'hspc_gallery';

delete from client_grant_type where owner_id = (SELECT id from client_details where client_id = 'my_web_app');
delete from client_scope where owner_id = (SELECT id from client_details where client_id = 'my_web_app');
delete from client_details where client_id = 'my_web_app';


