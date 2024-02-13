create table person_on_probation_user
(
    id              serial
        constraint prisoner_pkey primary key,
    crn            varchar(30)           not null,
    email          varchar(100)           not null,
    cpr_id          varchar(30)           not null,
    verified        boolean default false,
    when_created    timestamp with time zone not null default now(),
    when_modified   timestamp with time zone not null default now()
);
