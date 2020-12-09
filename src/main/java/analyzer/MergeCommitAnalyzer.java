package analyzer;

import analyzer.reafactoring.Refactoring;
import analyzer.reafactoring.RefactoringFactory;
import db.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import utils.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MergeCommitAnalyzer {
    private Db db;
    private final Git git;
    private final ProjectData projectData;
    private final MergeCommit mergeCommit;
    private final RefactoringFactory refactoringFactory;

    public MergeCommitAnalyzer(
            Db db, Git git,
            ProjectData projectData,
            MergeCommit mergeCommit,
            RefactoringFactory refactoringFactory
    ) {
        this.db = db;
        this.git = git;
        this.projectData = projectData;
        this.mergeCommit = mergeCommit;
        this.refactoringFactory = refactoringFactory;
    }

    public void analyzeParallelRefactoring() throws IOException, GitAPIException, SQLException {

        var mergeRev = this.git.getRepository().parseCommit(ObjectId.fromString((mergeCommit.commitHash)));
        var mergeRevParents = new Pair<>(mergeRev.getParents());
        var baseRev = findMergeBase(mergeRevParents);

        if (baseRev == null) return;

        var firstRefactorings = getRefactorings(baseRev, mergeRevParents.first);
        var secondRefactorings = getRefactorings(baseRev, mergeRevParents.second);

        var allOverlaps = findParallelRefactorings(firstRefactorings, secondRefactorings);


        mergeCommit.baseCommitHash = baseRev.getName();
        mergeCommit.parallelRefactoringCount = allOverlaps.size();

        if (!allOverlaps.isEmpty()) {
            db.parallelRefactorings.create(allOverlaps);
        }

        db.mergeCommits.update(mergeCommit);
    }

    public Collection<ParallelRefactoring> findParallelRefactorings(Collection<Refactoring> firstBranchRefactorings,
                                                                    Collection<Refactoring> secondBranchRefactorings) {
        ArrayList<ParallelRefactoring> overlaps = new ArrayList<>();
        for (var refactoringOne : firstBranchRefactorings) {
            for (var refactoringTwo : secondBranchRefactorings) {
                if (refactoringOne.dbItem().commitHash.equals(refactoringTwo.dbItem().commitHash)) {
                    /*
                     * TODO: sometimes, same unit comes from both branches
                     *  This should not happen and needs to be fixed
                     *  For now, just skipping such pair should work
                     * */
                    continue;
                }

                if (refactoringOne.overlaps(refactoringTwo))
                    overlaps.add(new ParallelRefactoring(refactoringOne.dbItem(), refactoringTwo.dbItem(), mergeCommit));
            }
        }

        return overlaps;
    }

    private Collection<Refactoring> getRefactorings(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var refactoringCommitHashes = getRefactoringCommitHashes(from, to);
        if (refactoringCommitHashes.isEmpty()) {
            return new ArrayList<>();
        }

        return projectData
                .refactorings
                .stream()
                .filter(r -> refactoringCommitHashes.contains(r.commitHash))
                .map(refactoringFactory::create)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Set<String> getRefactoringCommitHashes(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var commits = git.log().addRange(from, to).call();
        var commitHashes = StreamSupport.stream(commits.spliterator(), false).map(c -> c.getId().getName()).collect(Collectors.toSet());
        commitHashes.retainAll(projectData.refactoringHashes);
        commitHashes.remove(from.name());

        return commitHashes;
    }

    private RevCommit findMergeBase(Pair<RevCommit> children) throws IOException {
        RevWalk walk = new RevWalk(git.getRepository());
        walk.setRevFilter(RevFilter.MERGE_BASE);
        walk.markStart(children.first);
        walk.markStart(children.second);
        var base = walk.next();
        if (base == null)
            return null;

        if (base.getName().equals(children.first.getName()) || base.getName().equals(children.second.getName()))
            return null;

        return base;
    }
}

