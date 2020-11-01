package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "refactoring_commit")
public class RefactoringCommit {
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = "commit_hash")
    public String commitHash;

    @DatabaseField(columnName = "project_id")
    public int projectId;
}

