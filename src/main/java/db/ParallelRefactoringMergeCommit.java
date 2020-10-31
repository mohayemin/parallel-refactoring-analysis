package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "parallel_refactoring_merge_commit")
public class ParallelRefactoringMergeCommit {
    @DatabaseField(id = true)
    public int id;
    @DatabaseField(columnName = "commit_hash")
    public String commitHash;
}
