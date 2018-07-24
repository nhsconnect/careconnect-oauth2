INSERT INTO client_details (client_id, client_name, logo_uri, access_token_validity_seconds, token_endpoint_auth_method) VALUES
  ('hspc_appointments', 'HSPC Appointments', 'https://content.hspconsortium.org/images/hspc-appointments/logo/appointments.png', 86400, 'NONE');

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'openid'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'profile'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'offline_access'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'launch'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'launch/patient'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'user/*.read'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'user/*.*'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'patient/*.read'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'patient/*.write'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'patient/Patient.read'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'patient/Patient.write'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'patient/Observation.read'),
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'patient/Observation.write');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'hspc_appointments'), 'authorization_code');