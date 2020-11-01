package db;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectData {
    public final Project project;
    public final List<MergeCommit> mergeCommits;
    public final List<RefactoringRegion> refactoringRegions;
    public final List<String> refactoringHashes;

    private ProjectData(Project project
            , List<MergeCommit> mergeCommits
            , List<RefactoringRegion> refactoringRegions
            , List<String> refactoringHashes) {
        this.project = project;
        this.mergeCommits = mergeCommits;
        this.refactoringRegions = refactoringRegions;
        this.refactoringHashes = refactoringHashes;
    }

    public static ProjectData load(Db db, String projectName) throws SQLException {
        var project = db.projects.queryForEq("name", projectName).get(0);
        var mergeCommits = db.mergeCommits.queryForEq("project_id", project.id);
        var refactoringHashes = db.refactoringCommits.queryForEq("project_id", project.id)
                .stream().map(c -> c.commitHash).collect(Collectors.toList());
        var refactoringRegions = db.refactoringRegions.queryForEq("project_id", project.id);

        return new ProjectData(project, mergeCommits, refactoringRegions, refactoringHashes);
    }
}
