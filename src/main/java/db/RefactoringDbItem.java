package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "refactoring")
public class RefactoringDbItem {
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = "refactoring_type")
    public String refactoringType;

    @DatabaseField(columnName = "refactoring_detail")
    public String refactoringDetail;

    @DatabaseField(columnName = "refactoring_commit_id")
    public int refactoringCommitId;

    @DatabaseField(columnName = "commit_hash")
    public String commitHash;

    @DatabaseField(columnName = "project_id")
    public int projectId;
}
