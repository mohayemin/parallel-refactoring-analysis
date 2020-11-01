package analyzer;

import db.*;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MergeCommitAnalyzer {
    private Db db;
    private final Git git;
    private final ProjectData projectData;
    private final MergeCommit mergeCommit;


    public MergeCommitAnalyzer(Db db, Git git, ProjectData projectData, MergeCommit mergeCommit) {
        this.db = db;
        this.git = git;
        this.projectData = projectData;
        this.mergeCommit = mergeCommit;
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

        db.mergeCommits.update(mergeCommit);
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

    private Collection<RefactoringRegion> getRefactoringRegions(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var hashes = getRefactoringRevList(from, to);
        if (hashes.isEmpty()){
            return new ArrayList<>();
        }

        return projectData.refactoringRegions.stream().filter(rr-> hashes.contains(rr.commitHash)).collect(Collectors.toList());
    }

    private Set<String> getRefactoringRevList(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var commits = git.log().addRange(from, to).call();
        var commitHashes = StreamSupport.stream(commits.spliterator(), false).map(c -> c.getId().getName()).collect(Collectors.toSet());
        commitHashes.retainAll(projectData.refactoringHashes);
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

