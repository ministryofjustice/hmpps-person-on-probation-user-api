
DELETE from person_on_probation_user;

INSERT INTO person_on_probation_user
(crn, email, cpr_id, verified, when_created, when_modified, noms_id, one_login_urn, prison_id, release_date)
VALUES('abc', 'user1@gmail.com', '123', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','G123','urn1', 'MDI', '2024-12-31');

INSERT INTO person_on_probation_user
(crn, email, cpr_id, verified, when_created, when_modified, noms_id, one_login_urn, prison_id, release_date)
VALUES('xyz', 'user2@gmail.com', '456', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566','G12345','urn2', 'MDI', '2024-12-31');