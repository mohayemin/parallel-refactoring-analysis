select pair.name refactoring_pair,
       count(pr.merge_commit_hash) total_count,
       IFNULL(sum(is_merge_conflicting), 0) conflicting_count,
       IFNULL(count(pr.merge_commit_hash) - sum(is_merge_conflicting), 0) non_conflicting_count
from refactoring_pair pair
         left join parallel_refactoring pr
                   on pair.name = pr.refactoring_pair
group by pair.name
order by total_count desc
;

