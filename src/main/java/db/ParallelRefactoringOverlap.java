package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "parallel_refactoring_overlap")
public class ParallelRefactoringOverlap {
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = "start_line")
    public int startLine;

    @DatabaseField
    public int length;

    @DatabaseField(columnName = "region1_id")
    public int region1Id;
    @DatabaseField(columnName = "region2_id")
    public int region2Id;

    @DatabaseField(columnName = "merge_commit_hash")
    public String mergeCommitHash;

    @DatabaseField(columnName = "project_id")
    public int projectId;

    public ParallelRefactoringOverlap() {
    }

    public ParallelRefactoringOverlap(RefactoringRegion region1, RefactoringRegion region2, String mergeCommitHash) {
        this.region1Id = region1.id;
        this.region2Id = region2.id;
        this.projectId = region1.projectId;
        this.mergeCommitHash = mergeCommitHash;

        this.startLine = Math.max(region1.startLine, region2.startLine);
        this.length = Math.min(region1.getEndLine(), region2.getEndLine()) - this.startLine;
    }
}
