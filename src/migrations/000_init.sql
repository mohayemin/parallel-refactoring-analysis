create table _migrations(
    id int auto_increment not null primary key ,
    script_file varchar(100) not null
);

insert into _migrations (script_file) value ('000_init.sql');
