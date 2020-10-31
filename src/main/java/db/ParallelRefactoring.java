package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "parallel_refactoring")
public class ParallelRefactoring {
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(columnName = "refactoring_commit1")
    private String refactoringCommit1;
    @DatabaseField(columnName = "refactoring_commit2")
    private String refactoringCommit2;
    @DatabaseField(columnName = "nearest_common_ancestor")
    private String nearestCommonAncestor;
    @DatabaseField(columnName = "project_id")
    public int projectId;

    public ParallelRefactoring() {

    }

    public ParallelRefactoring(String refactoringCommit1, String refactoringCommit2, String nearestCommonAncestor, int projectId)
    {
        this.refactoringCommit1 = refactoringCommit1;
        this.refactoringCommit2 = refactoringCommit2;
        this.nearestCommonAncestor = nearestCommonAncestor;
        this.projectId = projectId;
    }
}

