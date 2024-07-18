
    drop table if exists cider;

    drop table if exists customer;

    create table cider (
        cider_style tinyint check (cider_style between 0 and 4),
        price decimal(38,2),
        quantity_on_hand integer,
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        id varchar(36) not null,
        cider_name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table customer (
        version integer,
        created_date datetime(6),
        last_modified_date datetime(6),
        id varchar(36) not null,
        customer_name varchar(255),
        primary key (id)
    ) engine=InnoDB;

    drop table if exists cider;

    drop table if exists customer;

    create table cider (
        cider_style tinyint check (cider_style between 0 and 4),
        price decimal(38,2),
        quantity_on_hand integer,
        version integer,
        created_date datetime(6),
        update_date datetime(6),
        id varchar(36) not null,
        cider_name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    );-- engine=InnoDB;

    create table customer (
        version integer,
        created_date datetime(6),
        last_modified_date datetime(6),
        id varchar(36) not null,
        customer_name varchar(255),
        primary key (id)
    ) engine=InnoDB;
