package analyzer;

import db.Db;
import db.MergeCommit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MergeCommitAnalyzer {
    private Db db;
    private Git git;
    private MergeCommit mergeCommit;

    public MergeCommitAnalyzer(Db db, Git repository, MergeCommit mergeCommit) {
        this.db = db;
        this.git = repository;
        this.mergeCommit = mergeCommit;
    }

    public void analyzeParallelRefactoring(Set<String> refactoringHashes) throws IOException, GitAPIException {
        var mergeRev = this.git.getRepository().parseCommit(ObjectId.fromString((mergeCommit.commitHash)));
        var mergeRevParents = mergeRev.getParents();
        var baseRev = findMergeBase(mergeRevParents[0], mergeRevParents[1]);

        if (baseRev == null) return;

        var firstAncestry = getRevList(baseRev, mergeRevParents[0]);
        var secondAncestry = getRevList(baseRev, mergeRevParents[1]);

        firstAncestry.retainAll(refactoringHashes);
        secondAncestry.retainAll(refactoringHashes);

        mergeCommit.baseCommitHash = baseRev.getName();
        mergeCommit.branch1RefactoringCommitsCsv = String.join(",", firstAncestry);
        mergeCommit.branch2RefactoringCommitsCsv = String.join(",", secondAncestry);
    }

    private List<String> getRevList(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var commits = git.log().addRange(from, to).call();
        return StreamSupport.stream(commits.spliterator(), false).map(c -> c.getId().getName()).collect(Collectors.toList());
    }

    private RevCommit findMergeBase(ObjectId hash1, ObjectId hash2) throws IOException {
        RevWalk walk = new RevWalk(git.getRepository());
        walk.setRevFilter(RevFilter.MERGE_BASE);
        walk.markStart(walk.parseCommit(hash1));
        walk.markStart(walk.parseCommit(hash2));
        return walk.next();
    }
}

