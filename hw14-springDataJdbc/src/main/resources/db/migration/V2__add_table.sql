create table address
(
    id   bigserial not null primary key,
    street varchar(100),
    client_id bigint,
    FOREIGN KEY (client_id) REFERENCES client (id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(15),
    client_id bigint,
    FOREIGN KEY (client_id) REFERENCES client (id)
);