create table airports_raw
(
    job_id bigint not null,
    acronym varchar(3) not null,
    name varchar(255) not null,
    country varchar(255) not null comment 'country where the airport is located',
    lat decimal(10,8) not null comment 'lat of the airport location',
    lon decimal(10,8) not null comment 'lon of the airport location',
    primary key (job_id, acronym)
);