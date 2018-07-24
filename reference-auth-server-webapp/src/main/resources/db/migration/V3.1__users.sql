--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;



INSERT INTO users (username, password, enabled) VALUES
  ('al1','password',true),
  ('al2','password',true);



INSERT INTO authorities (username, authority) VALUES
  ('al1','ROLE_ADMIN'),
  ('al1','ROLE_USER'),
  ('al2','ROLE_ADMIN'),
  ('al2','ROLE_USER');

-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info (sub, preferred_username, name, email, email_verified) VALUES
  ('90341.ASDFJWFA','al1','Al1 Admin','kevin.mayfield@airelogic.com', true),
  ('01922.FLANRJQW','al2','Al2 Admin','jonathan.mew@airelogic.com', true);

--
-- Close the transaction and turn autocommit back on
--

COMMIT;



