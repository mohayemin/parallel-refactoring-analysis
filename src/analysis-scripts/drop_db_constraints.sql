/*
The database is very large and the foreign key constraints make the development process real slow sometimes
That is why I decided to drop the cosntraints
- Mohayemin
 */

alter table conflicting_java_file drop foreign key fk_conflicting_java_file_merge_commit1;
alter table conflicting_java_file drop index fk_conflicting_java_file_merge_commit1_idx;

alter table conflicting_region drop foreign key fk_conflicting_region_conflicting_java_file1;
alter table conflicting_region drop index fk_conflicting_region_conflicting_java_file1_idx;

alter table conflicting_region_history drop foreign key fk_conflicting_region_history_conflicting_region1;
alter table conflicting_region_history drop index fk_conflicting_region_history_conflicting_region1_idx;

alter table merge_commit drop foreign key fk_merge_commit_project;
alter table merge_commit drop index fk_merge_commit_project_idx;
-- alter table merge_commit drop foreign key commit_hash_UNIQUE;
alter table merge_commit drop index commit_hash_UNIQUE;

alter table refactoring drop foreign key  fk_refactoring_refactoring_commit1;
alter table refactoring drop index fk_refactoring_refactoring_commit1_idx;

alter table refactoring_commit drop foreign key fk_refactoring_commit_project1;
alter table refactoring_commit drop index fk_refactoring_commit_project1_idx;

alter table refactoring_region drop foreign key fk_refactoring_region_refactoring1;
alter table refactoring_region drop index fk_refactoring_region_refactoring1_idx;
