-- drop table parallel_refactoring;
create table parallel_refactoring (
	id int not null auto_increment primary key,
	refactoring_commit1 char(40) not null,
	refactoring_commit2 char(40) not null,
	nearest_common_ancestor char(40) not null,
	project_id int not null
);

insert into _migrations (script_file) value ('001_create_parallel_refactoring_table.sql');

