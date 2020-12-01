select is_conflicting 'is conflicting',
       count(*) 'total commits',
       sum(parallel_refactoring_count > 0) 'has parallel refactorings',
       sum(parallel_refactoring_count) 'total parallel refactorings'
from merge_commit
group by is_conflicting;
;
