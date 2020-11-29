package analyzer;

public class AnalysisOptions {
    public boolean skipProcessedProjects;
    public boolean skipLocallyUnavailableProjects;
    public String analysisRepositoryRoot;
    public DatabaseOptions dbOptions;
    public Integer[] projectIds;

    public AnalysisOptions(
            boolean skipProcessedProjects
            , boolean skipLocallyUnavailableProjects
            , String analysisRepositoryRoot
            , Integer[] projectIds
            , DatabaseOptions dbOptions
    ) {
        this.skipProcessedProjects = skipProcessedProjects;
        this.skipLocallyUnavailableProjects = skipLocallyUnavailableProjects;
        this.analysisRepositoryRoot = analysisRepositoryRoot;
        this.dbOptions = dbOptions;
        this.projectIds = projectIds;
    }

    public AnalysisOptions() {

    }
}

