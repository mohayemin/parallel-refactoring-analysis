select refactoring1_type
     , refactoring2_type
     , count(*) frequency
     , sum(is_merge_conflicting) conflicting
from parallel_refactoring
group by refactoring_pair
order by frequency desc