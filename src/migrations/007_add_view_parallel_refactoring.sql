drop view if exists parallel_refactoring;
create view parallel_refactoring as
select
    project_id,
    concat(refactoring1_type, ',', refactoring2_type) refactoring_pair,
    refactoring1_type,
    refactoring2_type,
    refactoring1_detail,
    refactoring2_detail,
    refactoring1_commit_id,
    refactoring2_commit_id,
    refactoring1_id,
    refactoring2_id,
    base_commit_hash,
    merge_commit_hash,
    is_merge_conflicting
from parallel_refactoring_overlap__refactoring__merge_commit
group by refactoring1_id, refactoring2_id
;

insert into _migrations (script_file) value ('007_add_view_parallel_refactoring.sql');

