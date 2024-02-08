create table "user"
(
    id              numeric
        constraint user_pkey primary key,
    crn            varchar(30)           not null,
    email          varchar(100)           not null,
    cpr_id          varchar(30)           not null,
    verified        boolean default false,
    when_created    timestamp with time zone not null default now(),
    when_modified   timestamp with time zone not null default now()
);

