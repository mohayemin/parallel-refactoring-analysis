drop view if exists parallel_refactoring_overlap__refactoring;

drop view if exists parallel_refactoring_overlap__refactoring__merge_commit ;
CREATE VIEW `parallel_refactoring_overlap__refactoring__merge_commit` AS
select `pro`.`id`                   AS `overlap_id`,
       `pro`.`project_id`           AS `project_id`,
       `pro`.`merge_commit_hash`    AS `merge_commit_hash`,
       `mc`.`base_commit_hash`      AS `base_commit_hash`,
       `mc`.`is_conflicting`        AS `is_merge_conflicting`,
        refactoring_pair(r1.refactoring_type, r2.refactoring_type) refactoring_pair,
       r1.commit_hash AS refactoring1_commit_hash,
       r2.commit_hash AS refactoring2_commit_hash,
       `r1`.`refactoring_commit_id` AS `refactoring1_commit_id`,
       `r2`.`refactoring_commit_id` AS `refactoring2_commit_id`,
       `pro`.`refactoring1_id`      AS `refactoring1_id`,
       `pro`.`refactoring2_id`      AS `refactoring2_id`,
       `r1`.`refactoring_type`      AS `refactoring1_type`,
       `r2`.`refactoring_type`      AS `refactoring2_type`,
       `r1`.`refactoring_detail`    AS `refactoring1_detail`,
       `r2`.`refactoring_detail`    AS `refactoring2_detail`
from (((`parallel_refactoring_overlap` `pro` left join `refactoring` `r1` on ((`pro`.`refactoring1_id` = `r1`.`id`))) left join `refactoring` `r2` on ((`pro`.`refactoring2_id` = `r2`.`id`)))
         left join `merge_commit` `mc` on ((`pro`.`merge_commit_hash` = convert(`mc`.`commit_hash` using utf8mb4))));

drop view if exists parallel_refactoring;
create view parallel_refactoring as
select project_id,
       refactoring_pair,
       refactoring1_commit_hash,
       refactoring2_commit_hash,
       refactoring1_detail,
       refactoring2_detail,
       refactoring1_commit_id,
       refactoring2_commit_id,
       refactoring1_id,
       refactoring2_id,
       base_commit_hash,
       merge_commit_hash,
       is_merge_conflicting,
       refactoring1_type,
       refactoring2_type
from parallel_refactoring_overlap__refactoring__merge_commit
group by refactoring1_id, refactoring2_id
;

insert into _migrations (script_file) value ('010_add_commit_hash_in_parallel_refactoring.sql');

