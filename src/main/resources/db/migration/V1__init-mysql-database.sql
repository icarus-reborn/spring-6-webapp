/*--check (cider_style between 0 and 4), removed from cider style*/
    drop table if exists cider;

    drop table if exists customer;

    create table cider (
        id varchar(36) not null,
        cider_style tinyint not null,
        price decimal(38,2),
        quantity_on_hand integer,
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        cider_name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table customer (
        id varchar(36) not null,
        version integer,
        created_date datetime(6),
        last_modified_date datetime(6),
        customer_name varchar(255),
        primary key (id)
    ) engine=InnoDB;

