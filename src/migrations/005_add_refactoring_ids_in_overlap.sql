alter table parallel_refactoring_overlap
add column refactoring1_id int not null,
add column refactoring2_id int not null
;

insert into _migrations (script_file) value ('005_add_refactoring_ids_in_overlap.sql');

