package db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.eclipse.jgit.lib.ObjectId;

import java.sql.SQLException;

public class Db {
    private ConnectionSource connection;
    public final Dao<Project, Integer> projects;
    public final Dao<RefactoringCommit, Integer> refactoringCommits;
    public final Dao<MergeCommit, Integer> mergeCommits;
    public final Dao<RefactoringRegion, Integer> refactoringRegions;
    public final Dao<ParallelRefactoringOverlap, Integer> parallelRefactoringOverlaps;

    public Db() throws SQLException {
        connection =
                new JdbcConnectionSource("jdbc:mysql://localhost:3306/parallel_refactoring_analysis", "root", "admin");
        projects = DaoManager.createDao(connection, Project.class);
        refactoringCommits = DaoManager.createDao(connection, RefactoringCommit.class);
        mergeCommits = DaoManager.createDao(connection, MergeCommit.class);
        refactoringRegions = DaoManager.createDao(connection, RefactoringRegion.class);
        parallelRefactoringOverlaps = DaoManager.createDao(connection, ParallelRefactoringOverlap.class);
    }

    public <T> void deleteByValue(Class<T> daoType, String propertyName, Object propertyValue) throws SQLException {
        var dao = DaoManager.lookupDao(connection, daoType);
        var builder = dao.deleteBuilder();
        builder.where().eq(propertyName, propertyValue);
        builder.delete();
    }
}
