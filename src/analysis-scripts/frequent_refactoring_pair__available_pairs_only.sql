select
--       project_id,
       refactoring_pair,
--       refactoring1_detail,
--       refactoring2_detail,
       count(refactoring1_commit_id) total_count,
       sum(is_merge_conflicting) conflicting_count
from parallel_refactoring
group by
--         project_id,
         refactoring_pair
order by
--         project_id,
         total_count desc
;




/*
 30 November 3:32 AM Bangladesh Time
 Move Class, Move Class pair is most frequent
 However, it is biased because just project 67 has most of such refactoring
 */