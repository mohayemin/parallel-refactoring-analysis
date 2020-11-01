package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.eclipse.jgit.lib.AnyObjectId;

@DatabaseTable(tableName = "merge_commit")
public class MergeCommit {
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = "project_id")
    public int projectId;

    @DatabaseField(columnName = "base_commit_hash")
    public String baseCommitHash;

    @DatabaseField(columnName = "branch1_refactoring_commits_csv")
    public String branch1RefactoringCommitsCsv;

    @DatabaseField(columnName = "branch2_refactoring_commits_csv")
    public String branch2RefactoringCommitsCsv;

    @DatabaseField(columnName = "commit_hash")
    public String commitHash;
}
