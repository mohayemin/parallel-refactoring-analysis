/*
[[--- CAUTION: LONG RUNNING SCRIPT ---]]
This is a long running script if run on the original refactoring analysis database
It may take half an hour to complete.
Make sure you drop the constraints before running this script.
Otherwise this script may take really very long time to complete.
Not sure how long, may be days.
There is a script to drop constraints
- Mohayemin
 */

set @project_count := 500;

delete from project where id > @project_count;
-- The following projects are no longer accessible at the given URL
delete from project where id in (61,127,166,205,360,438);

-- this project was making trouble in many ways
delete from project where id in (25);


delete from conflicting_java_file where project_id not in (select id from project);
delete from conflicting_region where project_id not in (select id from project);
delete from conflicting_region_history where project_id not in (select id from project);
delete from merge_commit where project_id not in (select id from project);
delete from refactoring where project_id not in (select id from project);
delete from refactoring_commit where project_id not in (select id from project);
delete from refactoring_region where project_id not in (select id from project);
