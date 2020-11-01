import analyzer.ProjectAnalyzer;
import db.Db;
import db.Project;
import db.ProjectData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static utils.Logger.*;

public class Application {
    public static void main(String[] args) throws SQLException, IOException {
        log("----------------------------");
        log("----------------------------");
        logWithTime("start");
        log("----------------------------");
        log("----------------------------");
        analyzeAllProjects();
        log("____________________________");
        log("____________________________");
        logWithTime("end");
        log("____________________________");
        log("____________________________");
    }

    private static void analyzeAllProjects() throws SQLException, IOException {
        var db = new Db();
        for (Project project : db.projects.queryForEq("is_done", true)) {
            try {
                analyzeProject(project);
            } catch (Exception e) {
                e.printStackTrace();
                log("Failed to analyze project " + project.name + ". Skipping");
            }
        }

        db.close();
    }

    private static void analyzeProject(Project project) throws SQLException, IOException, GitAPIException {
        log("======================================");
        log("======================================");
        log("======================================");
        logWithTime("Start analyzing %s\n", project);

        var gitDirectory = new File("/work/PhD/DS-CMPUT-605/Project/analysisRepositories" + "/" + project.name);
        if (!gitDirectory.exists()) {
            log("Local repository unavailable. Cloning from %s\n", project.url);
            Git.cloneRepository()
                    .setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
                    .setURI(project.url)
                    .setDirectory(gitDirectory)
                    .call();
            logWithTime("Done cloning\n");
        }
        var git = Git.open(gitDirectory);

        var startTime = System.currentTimeMillis();
        Db db = new Db();
        var projectData = ProjectData.load(db, project);

        log(projectData.mergeCommits.size() + " merge commits");
        log(projectData.refactoringHashes.size() + " refactoring commits");

        var analyzer = new ProjectAnalyzer(db, git, projectData);
        analyzer.analyze();

        db.close();
        git.gc();
        System.gc();

        var endTime = System.currentTimeMillis();
        logWithTime(projectData.summary() + " done in " + (endTime - startTime) / 1000.0 + " seconds");
    }
}
