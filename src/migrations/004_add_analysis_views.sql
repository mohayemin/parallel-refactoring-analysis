drop view if exists merge_commit__project;
create view merge_commit__project as
    select mc.*, p.name from
        merge_commit mc left join project p
            on mc.project_id = p.id
    where p.is_parallel_refactoring_analysis_done = true and p.is_done = true;


insert into _migrations (script_file) value ('004_add_analysis_views.sql');

