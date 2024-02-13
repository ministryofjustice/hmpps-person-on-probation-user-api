
DELETE from person_on_probation_user;

INSERT INTO person_on_probation_user
(id, crn, email, cpr_id, verified, when_created, when_modified)
VALUES(1, 'abc', 'user1@gmail.com', '123', 'false', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566');

INSERT INTO person_on_probation_user
(id, crn, email, cpr_id, verified, when_created, when_modified)
VALUES(2, 'xyz', 'user2@gmail.com', '456', 'false', '2024-02-12 14:33:26.520566','2024-02-12 14:33:26.520566');