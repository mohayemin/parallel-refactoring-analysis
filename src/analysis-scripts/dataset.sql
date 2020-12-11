-- Assumes that all projects are processed for parallel refactoring.

set @total_projects := (select count(id)  from project);
set @total_merges:= (select count(*) from merge_commit);
set @total_refactorings := (select count(*) from refactoring);

select @total_projects, @total_merges, @total_refactorings;
