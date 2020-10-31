create table message
(
    id      bigint        not null,
    tag     varchar(255),
    text    varchar(2048) not null,
    user_id bigint,
    primary key (id)
) engine = InnoDB;

create table user_role
(
    user_id bigint not null,
    roles   varchar(255)
) engine = InnoDB;

create table users
(
    id              bigint       not null,
    activation_code varchar(255),
    email           varchar(255),
    is_active       bit          not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    primary key (id)
) engine = InnoDB;

alter table message
    add constraint message_user_fk foreign key (user_id) references users (id);

alter table user_role
    add constraint user_role_user_fk foreign key (user_id) references users (id);