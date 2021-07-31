insert into address_data_set values (default, 'First street');
insert into client values (default, 'dbServiceFirst', (select max(id) from address_data_set where street = 'First street'));
insert into phone_data_set values (default, 'Phone 1', (select max(id) from client where name = 'dbServiceFirst'));
insert into phone_data_set values (default, 'Phone 2', (select max(id) from client where name = 'dbServiceFirst'));

insert into address_data_set values (default, 'Second street');
insert into client values (default, 'dbServiceSecond', (select max(id) from address_data_set where street = 'Second street'));
insert into phone_data_set values (default, 'Phone 3', (select max(id) from client where name = 'dbServiceSecond'));
insert into phone_data_set values (default, 'Phone 4', (select max(id) from client where name = 'dbServiceSecond'));