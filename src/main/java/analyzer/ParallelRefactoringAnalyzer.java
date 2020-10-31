package analyzer;

import db.Db;
import db.ParallelRefactoring;
import db.Project;
import org.eclipse.jgit.api.Git;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        System.out.printf("analyzing %s, id %d\n", project.name, project.id);
        var mergeCommitHashes = getMergeCommitHashes();
        var refactoringHashes = db.refactoringCommits.queryForEq("project_id", project.id)
                .stream()
                .map(rc -> rc.commitHash)
                .collect(Collectors.toSet());

        System.out.println(mergeCommitHashes.size() + " merge commits");
        System.out.println(refactoringHashes.size() + " refactoring commits");

        var count = 0;
        for (var mc : mergeCommitHashes) {
            var mergeAnalyzer = new MergeCommit(repository, mc);
            if (mergeAnalyzer.isParallelRefactoringMerge(refactoringHashes)){
                System.out.println(mc.getName());
                count++;
            }
        }

        System.out.println(count);
    }

    private RevCommit findMergeBase(ObjectId hash1, ObjectId hash2) throws IOException {
        RevWalk walk = new RevWalk(repository.getRepository());
        walk.setRevFilter(RevFilter.MERGE_BASE);
        walk.markStart(walk.parseCommit(hash1));
        walk.markStart(walk.parseCommit(hash2));
        return walk.next();
    }

    private List<RevCommit> getMergeCommitHashes() throws GitAPIException {
        var mergeCommits = repository.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        return StreamSupport.stream(mergeCommits.spliterator(), false).collect(Collectors.toList());
    }

    public List<ObjectId> getRevList(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var commits = repository.log().addRange(from, to).call();
        return StreamSupport.stream(commits.spliterator(), false).map(RevObject::getId).collect(Collectors.toList());
    }
}

