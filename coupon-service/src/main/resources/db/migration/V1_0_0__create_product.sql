-- create table USER_PROFILE(
--     id bigint(20) NOT NULL AUTO_INCREMENT,
--     username varchar(100) NOT NULL,
--     firstName varchar(50) NOT NULL,
--     lastName varchar(50) NOT NULL,
--     email varchar(50) NOT NULL,
--     PRIMARY KEY (id),
--     UNIQUE KEY UK_username (username)
-- );

create table product(
    id int NOT NULL AUTO_INCREMENT,
    product_name varchar(20) NOT NULL,
    product_description varchar(100) NOT NULL,
    product_price decimal(8, 3),
    PRIMARY KEY (id)
);

create table coupon(
    id int NOT NULL AUTO_INCREMENT,
    coupon_code varchar(20) NOT NULL,
    discount decimal(8, 3) NOT NULL,
    exp_date varchar(50),
    PRIMARY KEY (id)
);

