create table bases(
    id bigint(20) unsigned not null auto_increment,
    created datetime not null,
    counter int not null comment 'counter',
    primary key (id)
);