create table test_user_data
(
    user_id  varchar(7) PRIMARY KEY   not null,
    created_by       varchar(32)              not null,
    created_date_time timestamp with time zone not null default now(),
    modified_by       varchar(32)              not null,
    modified_date_time timestamp with time zone not null default now(),
    unique (user_id)
);
