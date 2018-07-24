--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;

create table IF NOT EXISTS users(
  username varchar(50) not null primary key,
  password varchar(50) not null,
  enabled boolean not null);

create table IF NOT EXISTS authorities (
  username varchar(50) not null,
  authority varchar(50) not null,
  constraint fk_authorities_users foreign key(username) references users(username),
  constraint ix_authority unique (username,authority));


INSERT INTO users (username, password, enabled) VALUES
  ('admin','password',true),
  ('demo','password',true);


INSERT INTO authorities (username, authority) VALUES
  ('admin','ROLE_ADMIN'),
  ('admin','ROLE_USER'),
  ('demo','ROLE_USER');

-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info (sub, preferred_username, name, email, email_verified) VALUES
  ('90342.ASDFJWFA','admin','Demo Admin','admin@example.com', true),
  ('01921.FLANRJQW','demo','Demo User','user@example.com', true);

--
-- Close the transaction and turn autocommit back on
--

COMMIT;



