-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence address_SEQ start with 1 increment by 1;

create table address
(
    id   bigint not null primary key,
    street varchar(100)
);
ALTER TABLE client ADD COLUMN address_id bigint;
ALTER TABLE client ADD FOREIGN KEY (address_id) REFERENCES address (id);

create sequence phone_SEQ start with 1 increment by 1;

create table phone
(
    id   bigint not null primary key,
    number varchar(15),
    client_id bigint,
    FOREIGN KEY (client_id) REFERENCES client (id)
);