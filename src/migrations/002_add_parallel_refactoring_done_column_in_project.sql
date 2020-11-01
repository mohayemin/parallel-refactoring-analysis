alter table project
    add column is_parallel_refactoring_analysis_done bit not null default false;

insert into _migrations (script_file) value ('002_add_parallel_refactoring_done_column_in_project.sql');
