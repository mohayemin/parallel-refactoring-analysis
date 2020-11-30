package analyzer;

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
import java.util.Arrays;

import static utils.Logger.log;
import static utils.Logger.logWithTime;

public class MainAnalyzer {
    private final AnalysisOptions options;

    public MainAnalyzer(AnalysisOptions options) {
        this.options = options;
    }

    public void analyze() throws SQLException, IOException {
        var db = new Db(options.dbOptions);

        var query = db.projects.queryBuilder().where();
        query.eq("is_done", true);
        if(options.projectIds != null && options.projectIds.length > 0)
            query.and().in("id", Arrays.asList(options.projectIds));
        if(options.skipProcessedProjects)
            query.and().eq("is_parallel_refactoring_analysis_done", false);

        for (Project project : db.projects.query(query.prepare())) {
            try {
                new ProjectAnalyzer(project, options).analyze();
            } catch (Exception e) {
                e.printStackTrace();
                log("Failed to analyze project " + project.name + ". Skipping");
            }
        }

        db.close();
    }
}
