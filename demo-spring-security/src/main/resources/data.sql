-- insert new user
INSERT
INTO
  users
  (username, password, enabled)
VALUES
  ('<username>', '<bcrypt pass>', true);

-- insert authority for username <username>
INSERT
INTO
  authorities
  (username, authority)
VALUES
  ('<username>', 'ROLE_ADMIN');
