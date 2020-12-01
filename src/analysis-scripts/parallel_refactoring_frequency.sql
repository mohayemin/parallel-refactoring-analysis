select @total_refactorings := count(*) from refactoring;

select @refactorings_with_parallel_refactoring:= count(distinct pr.refactoring_id)
from (
         select distinct refactoring1_id refactoring_id from parallel_refactoring
         union
         select distinct refactoring2_id refactoring_id from parallel_refactoring
     ) pr;

select @total_refactoring_commits := count(*) from refactoring_commit;

select @refactoring_commits_with_parallel_refactoring_commits := count(distinct pr.commit_id)
from (
         select distinct refactoring1_commit_id commit_id from parallel_refactoring
         union
         select distinct refactoring2_commit_id commit_id from parallel_refactoring
     ) pr;

select @total_refactorings,
       @refactorings_with_parallel_refactoring,
       @total_refactoring_commits,
       @refactoring_commits_with_parallel_refactoring_commits
;

