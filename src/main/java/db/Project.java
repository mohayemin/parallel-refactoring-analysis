package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "project")
public class Project {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String url;

    @DatabaseField(columnName = "is_done")
    public boolean isDone;

    @DatabaseField(columnName = "is_parallel_refactoring_analysis_done")
    public boolean isParallelRefactoringAnalysisDone;

    public Project() {

    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }
}

