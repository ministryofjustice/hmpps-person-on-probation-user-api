Drop table if exists "user";
Alter table person_on_probation_user add prison_id varchar(10);
Alter table person_on_probation_user add release_date date;
