
DELETE from delegated_access_permission;
DELETE from delegated_access;
DELETE from users;
ALTER SEQUENCE delegated_access_permission_id_seq RESTART WITH 1;
ALTER SEQUENCE delegated_access_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;

INSERT INTO users
(crn,  cpr_id, verified, when_created, when_modified, noms_id, one_login_urn)
VALUES('abc', '123', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','G123','urn1');

INSERT INTO users
(crn, cpr_id, verified, when_created, when_modified, noms_id, one_login_urn)
VALUES('null', 'null', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','null','urn3');
