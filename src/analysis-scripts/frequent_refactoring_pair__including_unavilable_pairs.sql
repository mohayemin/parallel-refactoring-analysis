select pair.name refactoring_pair,
       count(pr.refactoring_pair) total_count,
       sum(is_merge_conflicting) conflicting_count,
       count(pr.refactoring_pair) -sum(is_merge_conflicting) non_conflicting_count
from refactoring_pair pair
         left join parallel_refactoring pr
                   on pair.name = pr.refactoring_pair
group by pair.name
order by total_count desc
;

