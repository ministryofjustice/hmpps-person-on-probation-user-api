
DELETE from users;

INSERT INTO users
(crn,  cpr_id, verified, when_created, when_modified, noms_id, one_login_urn)
VALUES('abc', '123', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','G123','urn1');

INSERT INTO users
(crn, cpr_id, verified, when_created, when_modified, noms_id, one_login_urn)
VALUES('NA', 'NA', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','NA','urn3');
