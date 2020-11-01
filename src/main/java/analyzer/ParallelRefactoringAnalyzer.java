package analyzer;

import db.Db;
import db.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ParallelRefactoringAnalyzer {
    private final Db db;
    private final Project project;
    private final Git repository;

    public ParallelRefactoringAnalyzer(Db db, String analysisRepositoryRoot, Project project) throws IOException {
        this.db = db;
        this.project = project;
        this.repository = Git.open(new File(analysisRepositoryRoot + "/" + project.name));
    }

    public void analyze() throws GitAPIException, IOException, SQLException {
        cleanup();
        System.out.printf("analyzing %s, id %d\n", project.name, project.id);

        var mergeCommits = db.mergeCommits.queryForEq("project_id", project.id);

        var refactoringHashes = db.refactoringCommits.queryForEq("project_id", project.id)
                .stream()
                .map(rc -> rc.commitHash)
                .collect(Collectors.toSet());

        System.out.println(mergeCommits.size() + " merge commits");
        System.out.println(refactoringHashes.size() + " refactoring commits");

        for (var mc : mergeCommits) {
            var mergeAnalyzer = new MergeCommitAnalyzer(repository, mc);
            mergeAnalyzer.analyzeParallelRefactoring(refactoringHashes);
            db.mergeCommits.update(mc);
        }

        project.isParallelRefactoringAnalysisDone = true;
        db.projects.update(project);
    }

    public void cleanup() throws SQLException {
        project.isParallelRefactoringAnalysisDone = false;
        db.projects.update(project);

        var updateBuilder = db.mergeCommits.updateBuilder();
        updateBuilder.where().eq("project_id", project.id);
        updateBuilder
                .updateColumnValue("base_commit_hash", null)
                .updateColumnValue("branch1_refactoring_commits_csv", null)
                .updateColumnValue("branch2_refactoring_commits_csv", null)
        ;

        db.mergeCommits.update(updateBuilder.prepare());
    }
}

