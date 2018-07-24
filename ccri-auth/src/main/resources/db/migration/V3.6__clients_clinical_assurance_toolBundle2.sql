
INSERT INTO client_scope (owner_id, scope) VALUES
  ((SELECT id from client_details where client_id = 'clinical-assurance-tool'), 'user/Bundle.write');


