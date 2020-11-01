package analyzer;

import db.Db;
import db.ParallelRefactoringOverlap;
import db.ProjectData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.sql.SQLException;

public class ProjectAnalyzer {
    private Db db;
    private final Git git;
    private ProjectData projectData;

    public ProjectAnalyzer(Db db, Git git, ProjectData projectData){
        this.db = db;
        this.git = git;
        this.projectData = projectData;
    }

    public void analyze() throws GitAPIException, IOException, SQLException {
        cleanup();
        System.out.printf("analyzing %s, id %d\n", projectData.project.name, projectData.project.id);

        System.out.println(projectData.mergeCommits.size() + " merge commits");
        System.out.println(projectData.refactoringHashes.size() + " refactoring commits");

        var count = 0;
        for (var mc : projectData.mergeCommits) {
            var mergeAnalyzer = new MergeCommitAnalyzer(db, git, projectData, mc);
            mergeAnalyzer.analyzeParallelRefactoring();
            db.mergeCommits.update(mc);
            System.out.println("commit " + ++count + " of " + projectData.mergeCommits.size() + ": " + mc.parallelRefactoringCount + " parallel refactorings");
        }

        projectData.project.isParallelRefactoringAnalysisDone = true;
        db.projects.update(projectData.project);
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

