package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "merge_commit")
public class MergeCommit {
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = "project_id")
    public int projectId;

    @DatabaseField(columnName = "base_commit_hash")
    public String baseCommitHash;

    @DatabaseField(columnName = "commit_hash")
    public String commitHash;

    @DatabaseField(columnName = "parallel_refactoring_count")
    public int parallelRefactoringCount;

    @DatabaseField(columnName = "is_conflicting")
    public boolean isConflicting;
}
