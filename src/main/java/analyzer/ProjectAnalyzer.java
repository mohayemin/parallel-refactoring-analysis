package analyzer;

import db.Db;
import db.ParallelRefactoringOverlap;
import db.ProjectData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;

import java.io.IOException;
import java.sql.SQLException;

import static utils.Logger.log;

public class ProjectAnalyzer {
    private Db db;
    private final Git git;
    private ProjectData projectData;

    public ProjectAnalyzer(Db db, Git git, ProjectData projectData) {
        this.db = db;
        this.git = git;
        this.projectData = projectData;
    }

    public void analyze() throws GitAPIException, IOException, SQLException {
        cleanup();

        var count = 0;
        for (var mc : projectData.mergeCommits) {
            analyzeMerge(mc);
            log("merge " + ++count + " of " + projectData.mergeCommits.size() + ": " + mc.parallelRefactoringCount + " parallel refactorings");
        }

        projectData.project.isParallelRefactoringAnalysisDone = true;
        db.projects.update(projectData.project);
    }

    private void analyzeMerge(db.MergeCommit mc) throws IOException, GitAPIException, SQLException {
        var mergeAnalyzer = new MergeCommitAnalyzer(db, git, projectData, mc);
        try {
            mergeAnalyzer.analyzeParallelRefactoring();
        } catch (MissingObjectException e) {
            log("skipping unknown commit " + mc.commitHash);
        }

        db.mergeCommits.update(mc);
    }

    public void cleanup() throws SQLException {
        projectData.project.isParallelRefactoringAnalysisDone = false;
        db.projects.update(projectData.project);

        db.deleteByValue(ParallelRefactoringOverlap.class, "project_id", projectData.project.id);

        var updateBuilder = db.mergeCommits.updateBuilder();
        updateBuilder.where().eq("project_id", projectData.project.id);
        updateBuilder
                .updateColumnValue("base_commit_hash", null)
        ;

        db.mergeCommits.update(updateBuilder.prepare());
    }
}

