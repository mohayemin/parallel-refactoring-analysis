import analyzer.ParallelRefactoringAnalyzer;
import db.Db;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import java.io.IOException;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException, IOException, GitAPIException {
        Db db = new Db();
        var project = db.projects.queryForEq("name", "junit").get(0);

        var analyzer = new ParallelRefactoringAnalyzer(db, "/work/PhD/DS-CMPUT-605/Project/analysisRepositories", project);
        analyzer.analyze();
    }
}
