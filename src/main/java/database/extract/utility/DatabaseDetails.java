package database.extract.utility;

import java.util.Map;

public class DatabaseDetails {
    private String environmentAndFileName;
    private String connectionURL;
    private String connectionUserId;
    private String connectionUserPassword;
    private String query;

    public DatabaseDetails(Map<String, String> dbDetails){
        environmentAndFileName = dbDetails.get("EnvironmentAndFileName");
        connectionURL = dbDetails.get("ConnectionURL");
        connectionUserId = dbDetails.get("ConnectionUserId");
        connectionUserPassword = dbDetails.get("ConnectionPassword");
        query = dbDetails.get("Query");
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public String getConnectionUserId() {
        return connectionUserId;
    }

    public String getConnectionUserPassword() {
        return connectionUserPassword;
    }

    public String getEnvironmentAndFileName() {
        return environmentAndFileName;
    }

    public String getQuery() {
        return query;
    }
}
