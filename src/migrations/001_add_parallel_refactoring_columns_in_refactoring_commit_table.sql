/*
alter table merge_commit
    drop column base_commit_hash,
    drop column branch1_refactoring_commits_csv,
    drop column branch2_refactoring_commits_csv,
    drop column branch1_has_refactoring,
    drop column branch2_has_refactoring,
    drop column has_parallel_refactoring
 */

alter table merge_commit
    add column base_commit_hash char(40) null,
    add column branch1_refactoring_commits_csv text null,
    add column branch2_refactoring_commits_csv text  null,
    add column branch1_has_refactoring bit as (branch1_refactoring_commits_csv != ',,') ,
    add column branch2_has_refactoring  bit as (branch2_refactoring_commits_csv != ',,'),
    add column has_parallel_refactoring bit as (branch1_has_refactoring and branch2_has_refactoring)
;

insert into _migrations (script_file) value ('001_add_parallel_refactoring_columns_in_refactoring_commit_table.sql');

