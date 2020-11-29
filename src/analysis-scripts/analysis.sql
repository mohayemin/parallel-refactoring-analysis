select count(id) as analyzed_projects_count from project where is_parallel_refactoring_analysis_done = true;

select count(distinct  merge_commit_id) as grand_total_conflicting_merge from conflicting_region;
select count(id) grand_total_conflicts from merge_commit where is_conflicting = true;

select count(id) analyzed_merge_count from merge_commit__project;


select parallel_refactoring_count>0 as has_parallel_refactoring, is_conflicting, count(id) merge_count from
    merge_commit__project
group by is_conflicting, parallel_refactoring_count > 0
;
