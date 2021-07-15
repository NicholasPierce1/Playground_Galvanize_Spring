insert into Customer(id, first_name, last_name) values (1, 'John', 'Doe');
insert into Customer(id, first_name, last_name) values (2, 'Jane', 'Doe');

insert into Lesson(id, name, delivered_on) values (1, 'Spring', TO_DATE('2021-06-30', 'yyyy-MM-dd'));
insert into Lesson(id, name, delivered_on) values (2, 'Angular', TO_DATE('2021-07-07', 'yyyy-MM-dd'));

insert into address(id, address_path) values (1, 'some address');
insert into address(id, address_path) values (2, 'another address');

insert into user(id, name, address_id) values (1, 'John', 1);
insert into user(id, name, address_id) values (2, 'Bob', 2);

insert into address_custom(address_id, address_path) values (1, 'some address');
insert into address_custom(address_id, address_path) values (2, 'another address');

insert into user_custom(user_id, name, address) values (1, 'John', 1);
insert into user_custom(user_id, name, address) values (2, 'Bob', 2);