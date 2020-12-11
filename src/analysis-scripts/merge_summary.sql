select is_conflicting,
       count(*) total_merge_commits_in_category,
       sum(parallel_refactoring_count > 0) merge_commits_having_parallel_refactorings,
       sum(parallel_refactoring_count) total_parallel_refactorings_in_category
from merge_commit
group by is_conflicting;
;
