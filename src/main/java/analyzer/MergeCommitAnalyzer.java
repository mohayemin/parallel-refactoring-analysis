package analyzer;

import db.Db;
import db.MergeCommit;
import db.ParallelRefactoringOverlap;
import db.RefactoringRegion;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MergeCommitAnalyzer {
    private final Db db;
    private final Git git;
    private final MergeCommit mergeCommit;
    private final List<String> refactoringCommitHashes;

    public MergeCommitAnalyzer(Db db, Git repository, MergeCommit mergeCommit, List<String> refactoringCommitHashes) {
        this.db = db;
        this.git = repository;
        this.mergeCommit = mergeCommit;
        this.refactoringCommitHashes = refactoringCommitHashes;
    }

    public void analyzeParallelRefactoring() throws IOException, GitAPIException, SQLException {
        var mergeRev = this.git.getRepository().parseCommit(ObjectId.fromString((mergeCommit.commitHash)));
        var mergeRevParents = mergeRev.getParents();
        var baseRev = findMergeBase(mergeRevParents[0], mergeRevParents[1]);

        if (baseRev == null) return;

        var firstRegions = getRefactoringRegions(baseRev, mergeRevParents[0]);
        var secondRegions = getRefactoringRegions(baseRev, mergeRevParents[1]);

        var allOverlaps = findOverlaps(firstRegions, secondRegions);

        mergeCommit.baseCommitHash = baseRev.getName();
        mergeCommit.parallelRefactoringCount = allOverlaps.size();

        if (!allOverlaps.isEmpty()) {
            db.parallelRefactoringOverlaps.create(allOverlaps);
        }
    }

    public Collection<ParallelRefactoringOverlap> findOverlaps(Collection<RefactoringRegion> one, Collection<RefactoringRegion> two) {
        ArrayList<ParallelRefactoringOverlap> overlaps = new ArrayList<>();
        for (var unitOne : one) {
            for (var unitTwo : two) {
                if (unitOne.overlaps(unitTwo))
                    overlaps.add(new ParallelRefactoringOverlap(unitOne, unitTwo, mergeCommit.commitHash));
            }
        }

        return overlaps;
    }

    private Collection<RefactoringRegion> getRefactoringRegions(ObjectId from, ObjectId to) throws SQLException, IOException, GitAPIException {
        var hashes = getRefactoringRevList(from, to);
        if (hashes.isEmpty()){
            return new ArrayList<>();
        }

        var qb = db.refactoringRegions.queryBuilder();
        qb.where().in("commit_hash", hashes);
        return db.refactoringRegions.query(qb.prepare());
    }

    private Set<String> getRefactoringRevList(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var commits = git.log().addRange(from, to).call();
        var commitHashes = StreamSupport.stream(commits.spliterator(), false).map(c -> c.getId().getName()).collect(Collectors.toSet());
        commitHashes.retainAll(refactoringCommitHashes);
        commitHashes.remove(from.name());

        return commitHashes;
    }

    private RevCommit findMergeBase(ObjectId hash1, ObjectId hash2) throws IOException {
        RevWalk walk = new RevWalk(git.getRepository());
        walk.setRevFilter(RevFilter.MERGE_BASE);
        walk.markStart(walk.parseCommit(hash1));
        walk.markStart(walk.parseCommit(hash2));
        return walk.next();
    }
}

