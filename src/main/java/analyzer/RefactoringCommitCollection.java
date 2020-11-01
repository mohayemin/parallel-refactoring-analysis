package analyzer;

import db.RefactoringCommit;
import db.RefactoringRegion;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RefactoringCommitCollection {
    private final Map<String, RefactoringCommit> commitMap;
    private final Collection<RefactoringRegion> regions;

    public RefactoringCommitCollection(Collection<RefactoringCommit> commits, Collection<RefactoringRegion> regions) {
        this.commitMap = commits.stream().collect(Collectors.toMap(c -> c.commitHash, c -> c));
        this.regions = regions;
    }

    public Set<String> getHashes() {
        return this.commitMap.keySet();
    }

    public int commitSize() {
        return commitMap.size();
    }
}

