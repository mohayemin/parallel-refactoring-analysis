package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "parallel_refactoring")
public class ParallelRefactoring {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(columnName = "merge_commit_hash")
    public String mergeCommitHash;
    @DatabaseField(columnName = "refactoring1_id")
    public int refactoring1Id;
    @DatabaseField(columnName = "refactoring2_id")
    public int refactoring2Id;

    // below are redundant columns for analytics
    @DatabaseField(columnName = "base_commit_hash")
    public String baseCommitHash;

    @DatabaseField(columnName = "refactoring_pair")
    public String refactoringPair;

    @DatabaseField(columnName = "refactoring1_type")
    public String refactoring1Type;
    @DatabaseField(columnName = "refactoring2_type")
    public String refactoring2Type;

    @DatabaseField(columnName = "refactoring1_commit_hash")
    public String refactoring1CommitHash;
    @DatabaseField(columnName = "refactoring2_commit_hash")
    public String refactoring2CommitHash;

    @DatabaseField(columnName = "refactoring1_detail")
    public String refactoring1Detail;
    @DatabaseField(columnName = "refactoring2_detail")
    public String refactoring2Detail;

    @DatabaseField(columnName = "refactoring1_commit_id")
    public int refactoring1CommitId;
    @DatabaseField(columnName = "refactoring2_commit_id")
    public int refactoring2CommitId;

    @DatabaseField(columnName = "is_merge_conflicting")
    public boolean isMergeConflicting;

    @DatabaseField(columnName = "project_id")
    public int projectId;

    public ParallelRefactoring() {
    }

    public ParallelRefactoring(RefactoringDbItem refactoring1, RefactoringDbItem refactoring2, MergeCommit mergeCommit) {
        if (refactoring1.refactoringType.compareTo(refactoring2.refactoringType) > 0) {
            // the reordering is done for the sake of consistency in the pair
            // this will make analysis easier
            var copyOf2 = refactoring2;
            refactoring2 = refactoring1;
            refactoring1 = copyOf2;
        }

        mergeCommitHash = mergeCommit.commitHash;
        refactoring1Id = refactoring1.id;
        refactoring2Id = refactoring2.id;

        baseCommitHash = mergeCommit.baseCommitHash;
        refactoring1Type = refactoring1.refactoringType;
        refactoring2Type = refactoring2.refactoringType;
        refactoringPair = refactoring1Type + "," + refactoring2Type;
        refactoring1CommitHash = refactoring1.commitHash;
        refactoring2CommitHash = refactoring2.commitHash;
        refactoring1Detail = refactoring1.refactoringDetail;
        refactoring2Detail = refactoring2.refactoringDetail;
        refactoring1CommitId = refactoring1.refactoringCommitId;
        refactoring2CommitId = refactoring2.refactoringCommitId;
        isMergeConflicting = mergeCommit.isConflicting;
        this.projectId = refactoring1.projectId;
    }
}
