select
    project_id,
    refactoring1_type,
    refactoring2_type,
    count(overlap_id) occurrence,
    sum(is_merge_conflicting) conflicting
from parallel_refactoring_overlap__refactoring__merge_commit
group by project_id, refactoring1_type, refactoring2_type
order by occurrence desc

/*
 30 November 3:32 AM Bangladesh Time
 Move Class, Move Class pair is most frequent
 However, it is biased because just project 67 has most of such refactoring
 */