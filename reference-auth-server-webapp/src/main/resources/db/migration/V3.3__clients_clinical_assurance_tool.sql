INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
  ('clinical-assurance-tool', 'AM3ai-PGoZZRW-7osWbzvGlDBHjHq7M2aBlpNttreHeEyB5jequWy8fsHMVQP4JV0Kd0Fzrtu0iNEqGqguq69Qs', 'NHSD Clincal Assurance Tool', false, null, 3600, 600, true);

INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-assurance-tool'), 'user/Patient.read'),
  ((SELECT id from client_details where client_id = 'clinical-assurance-tool'), 'user/DocumentReference.read'),
  ((SELECT id from client_details where client_id = 'clinical-assurance-tool'), 'user/Binary.read'),
  ((SELECT id from client_details where client_id = 'clinical-assurance-tool'), 'smart/orchestrate_launch');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ((SELECT id from client_details where client_id = 'clinical-assurance-tool'), 'authorization_code');
