package analyzer;

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
import java.util.Arrays;

import static utils.Logger.log;
import static utils.Logger.logWithTime;

public class MainAnalyzer {
    private final AnalysisOptions options;

    public MainAnalyzer(AnalysisOptions options) {
        this.options = options;
    }

    public void analyze() throws SQLException, IOException {
        var db = new Db(options.dbOptions);

        var query = db.projects.queryBuilder().where();
        query.eq("is_done", true);
        if(options.projectIds != null)
            query.and().in("id", Arrays.asList(options.projectIds));
        if(options.skipProcessedProjects)
            query.and().eq("is_parallel_refactoring_analysis_done", false);

        for (Project project : db.projects.query(query.prepare())) {
            try {
                analyzeProject(project);
            } catch (Exception e) {
                e.printStackTrace();
                log("Failed to analyze project " + project.name + ". Skipping");
            }
        }

        db.close();
    }

    private void analyzeProject(Project project) throws SQLException, IOException, GitAPIException {
        log("======================================");
        log("======================================");
        log("======================================");
        logWithTime("Start analyzing %s\n", project);

        var gitDirectory = new File(options.analysisRepositoryRoot + "/" + project.name);
        if (!gitDirectory.exists()) {
            log("Local repository unavailable at ." + gitDirectory.getAbsolutePath());
            if(options.skipLocallyUnavailableProjects){
                log("Skipping");
                return;
            } else {
                cloneProject(project, gitDirectory);
            }
        }
        var git = Git.open(gitDirectory);

        var startTime = System.currentTimeMillis();
        Db db = new Db(options.dbOptions);
        var projectData = ProjectData.load(db, project);

        log(projectData.mergeCommits.size() + " merge commits");
        log(projectData.refactoringHashes.size() + " refactoring commits");

        var analyzer = new ProjectAnalyzer(db, git, projectData);
        analyzer.analyze();

        db.close();
        git.gc();
        System.gc();

        var endTime = System.currentTimeMillis();
        logWithTime(projectData.summary() + ", done in " + (endTime - startTime) / 1000.0 + " seconds");
    }

    private void cloneProject(Project project, File gitDirectory) throws GitAPIException {
        log("Cloning from %s\n", project.url);
        Git.cloneRepository()
                .setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
                .setURI(project.url)
                .setDirectory(gitDirectory)
                .call();
        logWithTime("Done cloning\n");
    }
}
