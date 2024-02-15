
DELETE from person_on_probation_user;

INSERT INTO person_on_probation_user
(crn, email, cpr_id, verified, when_created, when_modified)
VALUES('abc', 'user1@gmail.com', '123', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566');

INSERT INTO person_on_probation_user
(crn, email, cpr_id, verified, when_created, when_modified)
VALUES('xyz', 'user2@gmail.com', '456', 'true', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566');