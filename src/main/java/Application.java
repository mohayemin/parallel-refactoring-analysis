import analyzer.ProjectAnalyzer;
import db.Db;
import db.ProjectData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException, IOException, GitAPIException {
        Db db = new Db();

        var startTime = System.currentTimeMillis();

        var projectData = ProjectData.load(db, "junit");
        var git = Git.open(new File("/work/PhD/DS-CMPUT-605/Project/analysisRepositories" + "/" + projectData.project.name));
        var analyzer = new ProjectAnalyzer(db, git, projectData);
        analyzer.analyze();

        var endTime = System.currentTimeMillis();

        System.out.println("Done processing " + projectData.project.name + " in " + (endTime - startTime) / 1000.0 + " seconds");
        db.close();
    }
}
