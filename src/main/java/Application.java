import analyzer.AnalysisOptions;
import analyzer.DatabaseOptions;
import analyzer.MainAnalyzer;
import analyzer.ProjectAnalyzer;
import db.Db;
import db.Project;
import db.ProjectData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static utils.Logger.*;

public class Application {
    static Integer[] projectIds = {
            9,  // 2   parallel refactorings
//            11, // 1   parallel refactorings
//            13, // 1   parallel refactorings
//            23, // 1   parallel refactorings
//            25, // 476 parallel refactorings
//            26, // 1   parallel refactorings
//            27, // 1   parallel refactorings
            31, // 4   parallel refactorings
//            36, // 63  parallel refactorings
//            45, // 39  parallel refactorings
//            46, // 1   parallel refactorings
//            47, // 1   parallel refactorings
            48, // 3   parallel refactorings
//            49, // 1   parallel refactorings
            67, // 6   parallel refactorings
//            70, // 2   parallel refactorings
//            71, // 1   parallel refactorings
            73, // 6   parallel refactorings
//            76, // 2   parallel refactorings
            77, // 3   parallel refactorings
            78, // 3   parallel refactorings
            0
    };

    public static void main(String[] args) throws SQLException, IOException {
        log("----------------------------");
        log("----------------------------");
        logWithTime("start");
        log("----------------------------");
        log("----------------------------");
        var dbOptions = new DatabaseOptions("localhost:3366", "refactring_analysis_full", "root", "admin");
        new MainAnalyzer(new AnalysisOptions(false,
                true,
                "D:\\PhD\\DS-CMPUT-605\\Project\\analysisRepositories",
                projectIds,
                dbOptions
        )).analyze();
        log("____________________________");
        log("____________________________");
        logWithTime("end");
        log("____________________________");
        log("____________________________");
    }
}
