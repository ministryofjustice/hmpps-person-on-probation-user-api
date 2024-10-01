Alter table person_on_probation_user rename to users;
Alter SEQUENCE person_on_probation_user_id_seq rename to users_id_seq;

create table permission(
    id          serial constraint permission_pkey primary key,
    name        varchar(30)    not null,
    description varchar(100) not null
);

create table delegated_access
(
    id              serial constraint delegate_access_pkey primary key,
    initiated_user_id     integer not null references users (id),
    delegated_user_id     integer not null references users (id),
    when_created timestamp with time zone not null,
    when_deleted timestamp with time zone
);


create table delegated_access_permission
(
    id               serial constraint delegate_access_permission_pkey primary key,
    delegated_access_id integer not null references delegated_access (id),
    permission_id    integer not null references permission (id),
    when_granted       timestamp with time zone not null,
    when_revoked       timestamp with time zone null
);


Insert into Permission (name, description)  values ('LicenceCondition', 'Permission to share the licence condition to non-probation user')