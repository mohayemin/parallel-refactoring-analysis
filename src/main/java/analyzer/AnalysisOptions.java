package analyzer;

public class AnalysisOptions {
    public boolean skipProcessedProjects;
    public boolean skipLocallyUnavailableProjects;
    public String analysisRepositoryRoot;
    public DatabaseOptions dbOptions;
    public Integer[] projectIds;

    /**
     * @param skipProcessedProjects the program sets a 'processing done' flag when a project is analyzed and
     *                              the results are stored. Set this value to true to skip reprocessing such
     *                              projects
     * @param skipLocallyUnavailableProjects the program clones the repositories which are not available in the
     *                                       {analysisRepositoryRoot} folder. Set this flag to true to skip a project
     *                                       that is not available.
     * @param analysisRepositoryRoot the folder where repositories for analysis will be cloned
     * @param projectIds a list of project id which you want to process. The project IDs are available in the project
     *                   table of the database. Set this to null or empty array to analyze all projects.
     * @param dbOptions Database options
     */
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

