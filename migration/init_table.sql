create table if not exists users
(
    id            bigint generated always as identity primary key,
    username      varchar(255) unique,
    password      varchar(255) unique,
    email         varchar(255),
    authenticated bool
);

create table if not exists roles
(
    id      bigint generated always as identity primary key,
    user_id bigint references users (id),
    name    varchar(255)
);

create table if not exists refresh_token
(
    id          uuid,
    user_id     bigint references users (id),
    token       varchar(255) unique         not null,
    expiry_date timestamp(6) with time zone not null,

    primary key (id)
);

-- create table if not exists user_roles
-- (
--     role_id bigint,
--     user_id bigint,
--
--     primary key (role_id, user_id)
-- );

alter table users owner to postgres;
alter table roles owner to postgres;
alter table refresh_token owner to postgres;
-- alter table user_roles owner to postgres;