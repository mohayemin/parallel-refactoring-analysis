DROP FUNCTION IF EXISTS refactoring_pair;
CREATE FUNCTION refactoring_pair(refactoring1 varchar(100), refactoring2 varchar(100))
    RETURNS VARCHAR(200)
    DETERMINISTIC NO SQL
BEGIN
    set @small := least(refactoring1, refactoring2);
    set @big := greatest(refactoring1, refactoring2);
    return concat(@small, ',', @big);
END;

drop view if exists parallel_refactoring;
create view parallel_refactoring as
select
    project_id,
    refactoring_pair(refactoring1_type, refactoring2_type) refactoring_pair,
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

drop view if exists parallel_refactoring_overlap__refactoring;
create view parallel_refactoring_overlap__refactoring as
select
    pro.id overlap_id,
    pro.merge_commit_hash,
    pro.project_id,
    refactoring_pair(r1.refactoring_type, r2.refactoring_type) refactoring_pair,
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

drop view if exists refactoring_pair;
create view refactoring_pair as
(
select refactoring_pair(r1.refactoring_type, r2.refactoring_type) name
from (
      (select distinct refactoring_type from refactoring) as r1
         join
     (select distinct refactoring_type from refactoring) as r2
     on r1.refactoring_type >= r2.refactoring_type
         )
    );

insert into _migrations (script_file) value ('009_fix_refactoring_pair_consistency.sql');
