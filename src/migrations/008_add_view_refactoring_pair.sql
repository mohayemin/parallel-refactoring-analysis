drop view if exists refactoring_pair;
create view refactoring_pair as
(
select concat(r1.refactoring_type, ',', r2.refactoring_type) name
from (
          (select distinct refactoring_type from refactoring) as r1
         join
         (select distinct refactoring_type from refactoring) as r2
         on r1.refactoring_type >= r2.refactoring_type
    )
);

insert into _migrations (script_file) value ('008_add_view_refactoring_pair.sql');

