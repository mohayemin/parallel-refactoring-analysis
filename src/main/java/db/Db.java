package db;

import analyzer.DatabaseOptions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.sql.SQLException;

public class Db {
    private ConnectionSource connection;
    public final Dao<Project, Integer> projects;
    public final Dao<RefactoringCommit, Integer> refactoringCommits;
    public final Dao<MergeCommit, Integer> mergeCommits;
    public final Dao<ParallelRefactoring, Integer> parallelRefactorings;
    public final Dao<RefactoringDbItem, Integer> refactorings;

    public Db(DatabaseOptions options) throws SQLException {
        connection = new JdbcConnectionSource("jdbc:mysql://" + options.serverUrl + "/" + options.databaseName,
                options.username,
                options.password);
        projects = DaoManager.createDao(connection, Project.class);
        refactoringCommits = DaoManager.createDao(connection, RefactoringCommit.class);
        mergeCommits = DaoManager.createDao(connection, MergeCommit.class);
        parallelRefactorings = DaoManager.createDao(connection, ParallelRefactoring.class);
        refactorings = DaoManager.createDao(connection, RefactoringDbItem.class);
    }

    public <T> void deleteByValue(Class<T> daoType, String propertyName, Object propertyValue) throws SQLException {
        var dao = DaoManager.lookupDao(connection, daoType);
        var builder = dao.deleteBuilder();
        builder.where().eq(propertyName, propertyValue);
        builder.delete();
    }

    public void close() throws IOException {
        connection.close();
    }
}
