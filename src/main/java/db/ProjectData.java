package db;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectData {
    public final Project project;
    public final List<MergeCommit> mergeCommits;
    public final List<String> refactoringHashes;
    public final List<RefactoringDbItem> refactorings;

    private ProjectData(Project project
            , List<MergeCommit> mergeCommits
            , List<String> refactoringHashes
            , List<RefactoringDbItem> refactorings
    ) {
        this.project = project;
        this.mergeCommits = mergeCommits;
        this.refactoringHashes = refactoringHashes;
        this.refactorings = refactorings;
    }

    public static ProjectData load(Db db, Project project) throws SQLException {
        var mergeCommits = db.mergeCommits.queryForEq("project_id", project.id);
        var refactoringHashes = db.refactoringCommits.queryForEq("project_id", project.id)
                .stream().map(c -> c.commitHash).collect(Collectors.toList());
        var refactorings = db.refactorings.queryForEq("project_id", project.id);
        return new ProjectData(project, mergeCommits, refactoringHashes, refactorings);
    }

    public static ProjectData load(Db db, int projectId) throws SQLException {
        var project = db.projects.queryForEq("id", projectId).get(0);
        return load(db, project);
    }

    public String summary() {
        var totalParallelRefactorings = mergeCommits.stream().mapToInt(mc->mc.parallelRefactoringCount).sum();
        return String.format("%s(%d), %d merges, %d refactorings, %d parallel refactorings in %d merges",
                project.name,
                project.id,
                mergeCommits.size(),
                refactoringHashes.size(),
                totalParallelRefactorings,
                mergeCommits.stream().filter(mc -> mc.parallelRefactoringCount > 0).count()
        );
    }
}
