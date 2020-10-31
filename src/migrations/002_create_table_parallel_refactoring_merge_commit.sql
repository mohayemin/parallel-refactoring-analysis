-- drop table parallel_refactoring_merge_commit;
create table parallel_refactoring_merge_commit (
	id int not null auto_increment primary key,
	merge_commit_hash char(40) not null,
	common_ancestor_hash char(40) null,
	branch1_refactoring_commits_csv text null,
	branch2_refactoring_commits_csv text  null,
	project_id int not null,
	branch1_has_refactoring bit as (branch1_refactoring_commits_csv != '') ,
	branch2_has_refactoring  bit as (branch2_refactoring_commits_csv != ''),
	has_parallel_refactoring bit as (branch1_has_refactoring and branch2_has_refactoring)
);

insert into _migrations (script_file) value ('002_parallel_refactoring_merge_commit.sql');

