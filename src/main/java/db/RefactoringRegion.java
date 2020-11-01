package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "refactoring_region")
public class RefactoringRegion {

    public RefactoringRegion() {
    }

    public RefactoringRegion(String path, int startLine, int length) {
        this.path = path;
        this.startLine = startLine;
        this.length = length;
    }

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = "start_line")
    public int startLine;

    @DatabaseField(columnName = "refactoring_commit_id")
    public int refactoringCommitId;

    @DatabaseField
    public int length;

    @DatabaseField
    public String path;

    @DatabaseField(columnName = "commit_hash")
    public String commitHash;

    @DatabaseField(columnName = "project_id")
    public int projectId;

    public int getEndLine() {
        return startLine + length;
    }

    public boolean overlaps(RefactoringRegion that) {
        return path.equals(that.path)
                && this.startLine <= that.getEndLine()
                && that.startLine <= this.getEndLine();
    }
}
