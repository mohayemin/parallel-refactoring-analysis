select r.refactoring_type,
       r.occurance occurance_in_refactoring,
       count(pr.merge_commit_hash) occurance_in_parallel_refactoring
from (select refactoring_type,
             count(*) occurance
      from refactoring
      group by refactoring_type) r
         left join parallel_refactoring pr
                   on r.refactoring_type = pr.refactoring1_type
                          or r.refactoring_type = pr.refactoring2_type
group by r.refactoring_type
order by occurance desc;

