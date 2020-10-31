package analyzer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MergeCommit {
    private Git git;
    private RevCommit mergeCommit;

    public MergeCommit(Git repository, RevCommit mergeCommit) {
        this.git = repository;
        this.mergeCommit = mergeCommit;
    }

    public boolean isParallelRefactoringMerge(Set<String> refactoringHashes) throws IOException, GitAPIException {
        var mergeParents = mergeCommit.getParents();
        var commonAncestor = findMergeBase(mergeParents[0], mergeParents[1]);

        if (commonAncestor == null) return false;

        var firstAncestry = getRevList(commonAncestor, mergeParents[0]);
        var secondAncestry = getRevList(commonAncestor, mergeParents[1]);

        firstAncestry.retainAll(refactoringHashes);
        secondAncestry.retainAll(refactoringHashes);

        return !firstAncestry.isEmpty() && !secondAncestry.isEmpty();
    }

    private List<String> getRevList(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var commits = git.log().addRange(from, to).call();
        return StreamSupport.stream(commits.spliterator(), false).map(c->c.getId().getName()).collect(Collectors.toList());
    }

    private RevCommit findMergeBase(ObjectId hash1, ObjectId hash2) throws IOException {
        RevWalk walk = new RevWalk(git.getRepository());
        walk.setRevFilter(RevFilter.MERGE_BASE);
        walk.markStart(walk.parseCommit(hash1));
        walk.markStart(walk.parseCommit(hash2));
        return walk.next();
    }
}

