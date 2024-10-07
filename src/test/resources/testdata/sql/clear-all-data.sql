
DELETE from delegated_access_permission;
DELETE from delegated_access;
DELETE from delegated_user_otp;
DELETE from users;
ALTER SEQUENCE delegated_user_otp_id_seq RESTART WITH 1;
ALTER SEQUENCE delegated_access_permission_id_seq RESTART WITH 1;
ALTER SEQUENCE delegated_access_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;



