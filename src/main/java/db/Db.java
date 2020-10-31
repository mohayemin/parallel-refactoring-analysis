package db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Map;

public class Db {
    private ConnectionSource connection;
    public final Dao<ParallelRefactoringMergeCommit, Integer> parallelRefactoringMergeCommits;
    public final Dao<ParallelRefactoring,Integer> parallelRefactorings;
    public final Dao<Project, Integer> projects;
    public final Dao<RefactoringCommit, Integer> refactoringCommits;

    public Db() throws SQLException {
        connection =
                new JdbcConnectionSource("jdbc:mysql://localhost:3306/parallel_refactoring_analysis", "root", "admin");
        parallelRefactoringMergeCommits = DaoManager.createDao(connection, ParallelRefactoringMergeCommit.class);
        parallelRefactorings = DaoManager.createDao(connection, ParallelRefactoring.class);
        projects = DaoManager.createDao(connection, Project.class);
        refactoringCommits = DaoManager.createDao(connection, RefactoringCommit.class);
    }
}
