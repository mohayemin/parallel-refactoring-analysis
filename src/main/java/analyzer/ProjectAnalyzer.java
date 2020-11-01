package analyzer;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import db.Db;
import db.ParallelRefactoringOverlap;
import db.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ProjectAnalyzer {
    private final Db db;
    private final Project project;
    private final Git repository;

    public ProjectAnalyzer(Db db, String analysisRepositoryRoot, Project project) throws IOException {
        this.db = db;
        this.project = project;
        this.repository = Git.open(new File(analysisRepositoryRoot + "/" + project.name));
    }

    public void analyze() throws GitAPIException, IOException, SQLException {
        var startTime = System.currentTimeMillis();
        cleanup();
        System.out.printf("analyzing %s, id %d\n", project.name, project.id);

        var mergeCommits = db.mergeCommits.queryForEq("project_id", project.id);

        var refactoringHashes = db.refactoringCommits.queryForEq("project_id", project.id).stream().map(c->c.commitHash).collect(Collectors.toList());

        System.out.println(mergeCommits.size() + " merge commits");
        System.out.println(refactoringHashes.size() + " refactoring commits");

        var count = 0;
        for (var mc : mergeCommits) {
            var mergeAnalyzer = new MergeCommitAnalyzer(db, repository, mc, refactoringHashes);
            mergeAnalyzer.analyzeParallelRefactoring();
            db.mergeCommits.update(mc);
            System.out.println("commit " + ++count + " of " + mergeCommits.size() + ": " + mc.parallelRefactoringCount + " parallel refactorings");
        }

        project.isParallelRefactoringAnalysisDone = true;
        db.projects.update(project);

        var endTime = System.currentTimeMillis();
        System.out.println("Done processing " + project.name + " in " + (endTime - startTime)/1000.0 + " seconds");
    }

    private RefactoringCommitCollection createRefactoringCommitCollection() throws SQLException {
        var commits = db.refactoringCommits.queryForEq("project_id", project.id);
        var regions = db.refactoringRegions.queryForEq("project_id", project.id);

        return new RefactoringCommitCollection(commits, regions);
    }

    public void cleanup() throws SQLException {
        project.isParallelRefactoringAnalysisDone = false;
        db.projects.update(project);

        db.deleteByValue(ParallelRefactoringOverlap.class, "project_id", project.id);

        var updateBuilder = db.mergeCommits.updateBuilder();
        updateBuilder.where().eq("project_id", project.id);
        updateBuilder
                .updateColumnValue("base_commit_hash", null)
        ;

        db.mergeCommits.update(updateBuilder.prepare());
    }
}

