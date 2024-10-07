
create table delegated_user_otp
(
    id              serial constraint delegated_user_otp_pkey primary key,
    user_id     integer not null references users (id),
    otp        varchar(10)    not null,
    when_created timestamp with time zone not null,
    expiry_date timestamp with time zone,
    email varchar(100) not null
);
