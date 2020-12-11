select r.*, pr.parallel_refactoring_frequency
from (select refactoring_type, count(*) refactoring_frequency
      from refactoring
      group by refactoring_type) r
         left join
     (select a.ref refactoring_type, sum(a.count) parallel_refactoring_frequency
      from (
               select refactoring1_type ref, count(*) count
               from parallel_refactoring
               group by refactoring1_type
               union all
               select refactoring2_type ref, count(*) count
               from parallel_refactoring
               group by refactoring2_type
           ) a
      group by ref) pr
     on r.refactoring_type = pr.refactoring_type
order by r.refactoring_frequency desc
;

228
212
440

select distinct refactoring_pair from parallel_refactoring
