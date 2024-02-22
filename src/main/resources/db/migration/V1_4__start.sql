Alter table person_on_probation_user add noms_id varchar(7) not null;
Alter table person_on_probation_user add one_login_urn varchar(100) not null;
Alter table person_on_probation_user add unique (one_login_urn)
