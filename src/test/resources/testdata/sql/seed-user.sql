
DELETE from person_on_probation_user;

INSERT INTO person_on_probation_user
(crn,  cpr_id, verified, when_created, when_modified, noms_id, one_login_urn)
VALUES('abc', '123', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','G123','urn1');

INSERT INTO person_on_probation_user
(crn, cpr_id, verified, when_created, when_modified, noms_id, one_login_urn)
VALUES('xyz', '456', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','G12345','urn2');