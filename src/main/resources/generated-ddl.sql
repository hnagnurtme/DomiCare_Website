
    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table payments (
        total_amount float(23),
        booking_id bigint not null,
        id bigint not null auto_increment,
        payment_date varchar(255),
        payment_method enum ('CASH','CREDIT_CARD','DEBIT_CARD','PAYPAL'),
        payment_status enum ('CANCELLED','PAID','PENDING'),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table payments 
       add constraint UKnuscjm6x127hkb15kcb8n56wo unique (booking_id);

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table payments 
       add constraint FKc52o2b1jkxttngufqp3t7jr3h 
       foreign key (booking_id) 
       references bookings (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss
       foreign key (user_id)
       references users (id);

    alter table users_roles
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy
       foreign key (role_id)
       references roles (id);

    alter table users_roles
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa
       foreign key (user_id)
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);

    create table booking_products (
        booking_id bigint not null,
        product_id bigint not null
    ) engine=InnoDB;

    create table bookings (
        is_periodic bit,
        total_hours float(53),
        total_price float(53),
        booking_date datetime(6),
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        user_id bigint not null,
        address varchar(255),
        create_by varchar(255),
        note varchar(255),
        update_by varchar(255),
        booking_status enum ('ACCEPTED','CANCELLED','PENDING','REJECTED'),
        primary key (id)
    ) engine=InnoDB;

    create table categories (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table files (
        is_deleted bit not null,
        created_at datetime(6),
        id bigint not null auto_increment,
        updated_at datetime(6),
        create_by varchar(255),
        name varchar(255),
        size varchar(255),
        type varchar(255),
        update_by varchar(255),
        url varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions (
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        api_path varchar(255),
        create_by varchar(255),
        method varchar(255),
        module varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table permissions_roles (
        permission_id bigint not null,
        role_id bigint not null
    ) engine=InnoDB;

    create table products (
        discount float(53),
        overal_rating float(53),
        price float(53),
        category_id bigint not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        image varchar(255),
        name varchar(255),
        update_by varchar(255),
        landing_images varbinary(255),
        primary key (id)
    ) engine=InnoDB;

    create table reviews (
        rating integer,
        create_at datetime(6),
        id bigint not null auto_increment,
        product_id bigint not null,
        update_at datetime(6),
        user_id bigint not null,
        comment varchar(255),
        create_by varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table roles (
        active bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        create_by varchar(255),
        description varchar(255),
        name varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table tokens (
        expiration datetime(6),
        id bigint not null auto_increment,
        user_id bigint not null,
        refresh_token varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
        is_email_confirmed bit not null,
        create_at datetime(6),
        id bigint not null auto_increment,
        update_at datetime(6),
        address varchar(255),
        avatar varchar(255),
        create_by varchar(255),
        email varchar(255),
        email_confirmation_token varchar(255),
        full_name varchar(255),
        google_id varchar(255),
        password varchar(255),
        phone varchar(255),
        update_by varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    alter table roles 
       add constraint UKofx66keruapi6vyqpv6f2or37 unique (name);

    alter table tokens 
       add constraint UK868xfj44b89t1voh058wevbqg unique (refresh_token);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UKdpvcvviwkmphd085ld3k1ebrv unique (email_confirmation_token);

    alter table users 
       add constraint UKovh8xmu9ac27t18m56gri58i1 unique (google_id);

    alter table booking_products 
       add constraint FK61i0xq10qbqmdoopgop57xyd9 
       foreign key (product_id) 
       references products (id);

    alter table booking_products 
       add constraint FKihcgeg3ds5wtdcgu3eqq446qo 
       foreign key (booking_id) 
       references bookings (id);

    alter table bookings 
       add constraint FKeyog2oic85xg7hsu2je2lx3s6 
       foreign key (user_id) 
       references users (id);

    alter table permissions_roles 
       add constraint FKff6bcp6bbaup2irutar3dfaks 
       foreign key (permission_id) 
       references permissions (id);

    alter table permissions_roles 
       add constraint FK9j7vx1vojmoa6rs21eggd46xn 
       foreign key (role_id) 
       references roles (id);

    alter table products 
       add constraint FKog2rp4qthbtt2lfyhfo32lsw9 
       foreign key (category_id) 
       references categories (id);

    alter table reviews 
       add constraint FKpl51cejpw4gy5swfar8br9ngi 
       foreign key (product_id) 
       references products (id);

    alter table reviews 
       add constraint FKcgy7qjc1r99dp117y9en6lxye 
       foreign key (user_id) 
       references users (id);

    alter table tokens 
       add constraint FK2dylsfo39lgjyqml2tbe0b0ss 
       foreign key (user_id) 
       references users (id);

    alter table users_roles 
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy 
       foreign key (role_id) 
       references roles (id);

    alter table users_roles 
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa 
       foreign key (user_id) 
       references users (id);
