package analyzer;

public class DatabaseOptions {
    public final String serverUrl;
    public final String databaseName;
    public final String username;
    public final String password;

    public DatabaseOptions(
            String serverUrl,
            String databaseName,
            String username,
            String password
    ) {

        this.serverUrl = serverUrl;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }
}
