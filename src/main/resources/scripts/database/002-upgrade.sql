create table users
(
    id         varchar(13)  not null primary key,
    created    datetime     not null,
    name       varchar(255) not null comment 'Name of the user',
    surname    varchar(255) not null comment 'Surname of the user',
    email      varchar(255) not null comment 'Email of the user',
    password   varchar(255) not null comment 'Password of the user, encoded in bcrypt',
    last_login datetime     null comment 'Last login date of the user'
);

create table roles
(
    id   bigint(20) unsigned not null auto_increment primary key,
    name varchar(255)        not null comment 'Name of the role'
);

create table users_with_roles
(
    user_id varchar(13)         not null references users (id),
    role_id bigint(20) unsigned not null references roles (id)
);

insert into users(id, created, name, surname, email, password)
values ('0AYZF7072Z501', now(), 'user-name', 'user-surname', 'user@email.com',
        '$2a$12$fiJ.tvifXmEgzvB0ZhcF4uKzSKU5pzvJa7bdJWyeTlEJL17J5XGl.'),
    ('OAYZFA06XJNAV', now(),'admin-name', 'admin-surname','admin@email.com',
     '$2a$12$2r02BtB4Uq7xwkPzZK.9WurcGn3j24piaPsyZ3pXFU4yck7PleUj6');

insert into roles(name)
values ('USER'), ('ADMIN');

insert into users_with_roles(user_id, role_id)
values((select users.id from users where email = 'user@email.com'), (select roles.id from roles where name = 'USER')),
      ((select users.id from users where email = 'admin@email.com'),(select roles.id from roles where name = 'USER')),
      ((select users.id from users where email = 'admin@email.com'),(select roles.id from roles where name = 'ADMIN'));
