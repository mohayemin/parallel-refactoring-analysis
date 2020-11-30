import analyzer.AnalysisOptions;
import analyzer.DatabaseOptions;
import analyzer.MainAnalyzer;

import java.io.IOException;
import java.sql.SQLException;

import static utils.Logger.log;
import static utils.Logger.logWithTime;

public class Application {
    static Integer[] projectIds = {

    };

    public static void main(String[] args) throws SQLException, IOException {
        log("----------------------------");
        log("----------------------------");
        logWithTime("start");
        log("----------------------------");
        log("----------------------------");

        //projectIds = null;
        var dbOptions = new DatabaseOptions("localhost:3306", "refactoring_analysis_full", "root", "admin");
        new MainAnalyzer(new AnalysisOptions(false,
                false,
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
