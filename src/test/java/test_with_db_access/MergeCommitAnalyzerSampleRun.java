package test_with_db_access;

import analyzer.DatabaseOptions;
import analyzer.MergeCommitAnalyzer;
import analyzer.reafactoring.RefactoringFactory;
import db.Db;
import db.ProjectData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MergeCommitAnalyzerSampleRun {
    @Test
    public void run() throws SQLException, IOException, GitAPIException {
        var db = new Db(new DatabaseOptions("localhost:3306", "parallel_refactoring_analysis", "root", "admin"));
        var pd = ProjectData.load(db, 45);
        var git = Git.open(new File("D:\\PhD\\DS-CMPUT-605\\Project\\analysisRepositories\\" + pd.project.name));

//        294
//        430

        var mc = pd.mergeCommits.stream().filter(m -> m.commitHash.equals("99a34497bfa4bc534dd8a088094fa6c6fef56f46")).findFirst().get();
        var mca = new MergeCommitAnalyzer(db, git, pd, mc, new RefactoringFactory());
        mca.analyzeParallelRefactoring();
    }
}
