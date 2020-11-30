drop view if exists parallel_refactoring_overlap__refactoring;
create view parallel_refactoring_overlap__refactoring as
    select
        pro.id overlap_id,
        pro.merge_commit_hash,
        pro.project_id,

        r1.refactoring_commit_id refactoring1_commit_id,
        refactoring1_id,
        r1.refactoring_type refactoring1_type,
        r1.refactoring_detail refactoring1_detail,

        r2.refactoring_commit_id refactoring2_commit_id,
        refactoring2_id,
        r2.refactoring_type refactoring2_type,
        r2.refactoring_detail refactoring2_detail
    from parallel_refactoring_overlap pro
             left join refactoring r1 on pro.refactoring1_id = r1.id
             left join refactoring r2 on pro.refactoring2_id = r2.id
                                             and r1.id != r2.id
;

insert into _migrations (script_file) value ('006_add_view_joining_overlapping__refactoring__merge.sql');

