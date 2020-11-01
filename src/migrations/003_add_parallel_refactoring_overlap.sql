create table parallel_refactoring_overlap
(
    id int auto_increment not null primary key,
    start_line int not null ,
    length int not null,
    region1_id int not null ,
    region2_id int not null ,
    merge_commit_hash char(40) not null ,
    project_id int not null
);

alter table merge_commit
    drop constraint fk_merge_commit_project;

alter table merge_commit
    DROP index fk_merge_commit_project_idx;

alter table merge_commit
    DROP constraint commit_hash_UNIQUE;


alter table merge_commit
    drop column has_parallel_refactoring,
    drop column branch1_has_refactoring,
    drop column branch2_has_refactoring,
    drop column branch1_refactoring_commits_csv,
    drop column branch2_refactoring_commits_csv,
    add column parallel_refactoring_count int
;

insert into _migrations (script_file) value ('003_add_parallel_refactoring_overlap.sql');

