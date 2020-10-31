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
    private final Map<ObjectId, Set<ObjectId>> mergeAncestry;

    public ParallelRefactoringAnalyzer(Db db, String analysisRepositoryRoot, Project project) throws IOException {
        this.db = db;
        this.project = project;
        this.repository = Git.open(new File(analysisRepositoryRoot + "/" + project.name));
        this.mergeAncestry = new HashMap<>();
    }

    public void analyze() throws GitAPIException, IOException, SQLException {
        System.out.printf("analyzing %s, id %d\n", project.name, project.id);
        buildAncestryOfMergeCommits();
        var refactoringHashes = db.refactoringCommits.queryForEq("project_id", project.id)
                .stream()
                .map(rc-> ObjectId.fromString(rc.commitHash))
                .collect(Collectors.toList());

        System.out.println(refactoringHashes.size() + " refactoring commits");
        analyzeAllHashes(refactoringHashes);
    }

    public boolean areParallelCommits(ObjectId hash1, ObjectId hash2) throws IOException {
        return areSplit(hash1, hash2) && areMerged(hash1, hash2);
    }

    private void buildAncestryOfMergeCommits() throws GitAPIException, IOException {
        var mergeCommits = repository.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        for (var mc : mergeCommits) {
            var ancestry = getMergeCommitAncestry(mc);
            mergeAncestry.put(mc, ancestry);
        }
    }

    /**
     * <pre>
     * ---a--b--c--d--x--
     *     \        /
     *      m--n--o
     * </pre>
     * Given x, returns x d, c, b, o, n, m
     *
     * @param mergeCommitHash merge commit
     * @return ancestry
     */
    public HashSet<ObjectId> getMergeCommitAncestry(ObjectId mergeCommitHash) throws GitAPIException, IOException {
        var mergeCommit = findCommit(mergeCommitHash);
        var mergeParents = mergeCommit.getParents();
        var commonAncestor = findMergeBase(mergeParents[0], mergeParents[1]);

        if (commonAncestor == null)
            return new HashSet<>();

        var ancestry = new HashSet<ObjectId>();
        ancestry.addAll(getRevList(commonAncestor, mergeParents[0]));
        ancestry.addAll(getRevList(commonAncestor, mergeParents[1]));

        return ancestry;
    }

    private void analyzeAllHashes(List<ObjectId> refactoringHashes) throws SQLException, IOException {
        var deleteBuilder = db.parallelRefactorings.deleteBuilder();
        deleteBuilder.where().eq("project_id", project.id);
        db.parallelRefactorings.delete(deleteBuilder.prepare());

        for (var i = 0; i < refactoringHashes.size() - 1; i++) {
            var commit1 = findCommit(refactoringHashes.get(i));
            for (var j = i + 1; j < refactoringHashes.size(); j++) {
                var commit2 = findCommit(refactoringHashes.get(j));
                analyzeRefactoringCommitPair(commit1, commit2);
            }
        }
    }

    public RevCommit findCommit(ObjectId hash) throws IOException {
        return repository.getRepository().parseCommit(hash);
    }

    private RevCommit findMergeBase(ObjectId hash1, ObjectId hash2) throws IOException {
        RevWalk walk = new RevWalk(repository.getRepository());
        walk.setRevFilter(RevFilter.MERGE_BASE);
        walk.markStart(walk.parseCommit(hash1));
        walk.markStart(walk.parseCommit(hash2));
        return walk.next();
    }

    private boolean areSplit(ObjectId hash1, ObjectId hash2) throws IOException {
        var base = findMergeBase(hash1, hash2);
        return base != null && !(base.getName().equals(hash1.getName()) || base.getName().equals(hash2.getName()));
    }

    private boolean areMerged(ObjectId hash1, ObjectId hash2) {
        return mergeAncestry.values().stream().anyMatch(ancestor -> ancestor.contains(hash1) && ancestor.contains(hash2));
    }

    private void analyzeRefactoringCommitPair(RevCommit commit1, RevCommit commit2) throws IOException, SQLException {
        if (!areParallelCommits(commit1, commit2)) {
            return;
        }

        var base = findMergeBase(commit1, commit2);

        var parallelRefactorings = new ParallelRefactoring[]
                {
                        new ParallelRefactoring(commit1.getName(), commit2.getName(), base.getName(), project.id),
                        new ParallelRefactoring(commit2.getName(), commit1.getName(), base.getName(), project.id)
                };

        db.parallelRefactorings.create(Arrays.asList(parallelRefactorings));

        System.out.println(parallelRefactorings[0]);
    }

    public List<ObjectId> getRevList(ObjectId from, ObjectId to) throws IOException, GitAPIException {
        var commits = repository.log().addRange(from, to).call();
        return StreamSupport.stream(commits.spliterator(), false).map(RevObject::getId).collect(Collectors.toList());
    }
}
