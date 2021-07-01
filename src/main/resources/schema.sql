create table Product(
    name varchar(255) not null unique,
    price double not null unique,
    id int auto_increment not null,
    Primary Key (id),
    Constraint price_valid check (price > 0)
);