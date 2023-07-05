-- 1 Step
CREATE TABLE PERSON (
    id bigserial primary key,
    name varchar(50) not null,
    age int check ( age >= 18 ),
    drive_license boolean default false,
    car_id int references CAR (id)
);

-- 2 Step
CREATE TABLE CAR (
    id bigserial primary key,
    brand varchar(50) not null,
    model varchar(50) not null,
    price int check ( price > 0 )
);