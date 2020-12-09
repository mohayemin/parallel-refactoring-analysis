drop view if exists parallel_refactoring_overlap__refactoring;
drop view if exists parallel_refactoring_overlap__refactoring__merge_commit;
drop view if exists parallel_refactoring;
drop table if exists parallel_refactoring_overlap;

create table parallel_refactoring
(
    id                       int auto_increment not null primary key,
    merge_commit_hash        char(40)           not null,
    base_commit_hash         char(40)           not null,
    refactoring1_id          int                not null,
    refactoring2_id          int                not null,

    -- redundant columns for analytics
    refactoring_pair         varchar(200)       not null,
    refactoring1_type        varchar(100)       not null,
    refactoring2_type        varchar(100)       not null,
    refactoring1_commit_hash char(40)           not null,
    refactoring2_commit_hash char(40)           not null,
    refactoring1_detail      varchar(2000)      not null,
    refactoring2_detail      varchar(2000)      not null,
    refactoring1_commit_id   int                not null,
    refactoring2_commit_id   int                not null,
    is_merge_conflicting     bit                not null,
    project_id               int                not null
);

insert into _migrations (script_file) value ('011_analyze_with_overlapping_program_element.sql');

