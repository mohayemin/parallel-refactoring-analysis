package db;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectData {
    public final Project project;
    public final List<MergeCommit> mergeCommits;
    public final List<RefactoringRegion> refactoringRegions;
    public final List<String> refactoringHashes;
    public final List<RefactoringDbItem> refactorings;

    private ProjectData(Project project
            , List<MergeCommit> mergeCommits
            , List<RefactoringRegion> refactoringRegions
            , List<String> refactoringHashes
            , List<RefactoringDbItem> refactorings
    ) {
        this.project = project;
        this.mergeCommits = mergeCommits;
        this.refactoringRegions = refactoringRegions;
        this.refactoringHashes = refactoringHashes;
        this.refactorings = refactorings;
    }

    public static ProjectData load(Db db, Project project) throws SQLException {
        var mergeCommits = db.mergeCommits.queryForEq("project_id", project.id);
        var refactoringHashes = db.refactoringCommits.queryForEq("project_id", project.id)
                .stream().map(c -> c.commitHash).collect(Collectors.toList());
        var refactoringRegions = db.refactoringRegions.queryForEq("project_id", project.id);
        var refactorings = db.refactorings.queryForEq("project_id", project.id);
        return new ProjectData(project, mergeCommits, refactoringRegions, refactoringHashes, refactorings);
    }

    public static ProjectData load(Db db, int projectId) throws SQLException {
        var project = db.projects.queryForEq("id", projectId).get(0);
        return load(db, project);
    }

    public String summary() {
        return String.format("%s(%d), %d merges, %d refactorings, %d refactoring regions, %d merges with parallel refactoring",
                project.name,
                project.id,
                mergeCommits.size(),
                refactoringHashes.size(),
                refactoringRegions.size(),
                mergeCommits.stream().filter(mc -> mc.parallelRefactoringCount > 0).count()
        );
    }
}
