create table hospital
(
    hospital_id           varchar(20)                        not null
        primary key,
    hospital_name         varchar(100)                       not null,
    hospital_city         varchar(20)                        not null,
    district              varchar(20)                        not null,
    capacity              int                                not null,
    director_name         varchar(100)                       not null,
    director_contact_no   varchar(12)                        not null,
    hospital_contact_no_1 varchar(12)                        not null,
    hospital_contact_no_2 varchar(12)                        not null,
    hospital_fax_no       varchar(12)                        not null,
    hospital_email        varchar(100)                       not null,
    status                varchar(20) default 'not reserved' not null
);

create table quarantine_center
(
    id                  varchar(20)                        not null
        primary key,
    center_name         varchar(100)                       not null,
    city                varchar(20)                        not null,
    district            varchar(20)                        not null,
    head_name           varchar(50)                        not null,
    head_contact_no     varchar(12)                        not null,
    center_contact_no_1 varchar(12)                        not null,
    center_contact_no_2 varchar(12)                        not null,
    capacity            int                                not null,
    status              varchar(20) default 'not reserved' not null
);

create table user
(
    id         varchar(20) not null
        primary key,
    first_name varchar(50) not null,
    contact_no varchar(12) not null,
    email      varchar(50) not null,
    user_name  varchar(50) not null,
    password   varchar(20) not null,
    role       varchar(20) not null,
    location   varchar(50) not null,
    constraint user_name
        unique (user_name)
);


