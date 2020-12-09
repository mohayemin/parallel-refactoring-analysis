package analyzer;

import analyzer.reafactoring.RefactoringFactory;
import db.Db;
import db.ParallelRefactoringOverlap;
import db.Project;
import db.ProjectData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static utils.Logger.log;
import static utils.Logger.logWithTime;

public class ProjectAnalyzer {
    private final Project project;
    private final AnalysisOptions options;
    private final Db db;

    public ProjectAnalyzer(Project project, AnalysisOptions options) throws SQLException {
        this.project = project;
        this.options = options;
        db = new Db(options.dbOptions);
    }

    public void analyze() throws GitAPIException, IOException, SQLException {
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
        var projectData = ProjectData.load(db, project);

        log(projectData.mergeCommits.size() + " merge commits");
        log(projectData.refactoringHashes.size() + " refactoring commits");

        cleanup(projectData);

        var count = 0;
        for (var mc : projectData.mergeCommits) {
            analyzeMerge(mc, git, projectData);
            log("merge " + ++count + " of " + projectData.mergeCommits.size() + ": " + mc.parallelRefactoringCount + " parallel refactorings");
        }

        projectData.project.isParallelRefactoringAnalysisDone = true;
        db.projects.update(projectData.project);


        db.close();
        git.gc();
        System.gc();

        var endTime = System.currentTimeMillis();
        logWithTime(projectData.summary() + ", done in " + (endTime - startTime) / 1000.0 + " seconds");
    }

    private void analyzeMerge(db.MergeCommit mc, Git git, ProjectData projectData) throws IOException, GitAPIException, SQLException {
        var mergeAnalyzer = new MergeCommitAnalyzer(db, git, projectData, mc, new RefactoringFactory());
        try {
            mergeAnalyzer.analyzeParallelRefactoring();
        } catch (MissingObjectException e) {
            log("skipping unknown commit " + mc.commitHash);
        }

        db.mergeCommits.update(mc);
    }

    public void cleanup(ProjectData projectData) throws SQLException {
        project.isParallelRefactoringAnalysisDone = false;
        db.projects.update(projectData.project);

        db.deleteByValue(ParallelRefactoringOverlap.class, "project_id", projectData.project.id);

        var updateBuilder = db.mergeCommits.updateBuilder();
        updateBuilder.where().eq("project_id", projectData.project.id);
        updateBuilder
                .updateColumnValue("base_commit_hash", null)
        ;

        db.mergeCommits.update(updateBuilder.prepare());
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

