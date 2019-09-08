package database.extract.utility;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Map;

public class WorkerThread implements Runnable {

    private DatabaseDetails dbDetails;

    public WorkerThread(Map<String, String> dbDetails) {
        this.dbDetails = new DatabaseDetails(dbDetails);
    }

    @Override
    public void run() {
        writeDataInCSV();
    }

    private void writeDataInCSV() {
        System.out.println("Extraction Started for= " + dbDetails.getEnvironmentAndFileName());
        try (Connection connection = DriverManager.getConnection(dbDetails.getConnectionURL(), dbDetails.getConnectionUserId(), dbDetails.getConnectionUserPassword());
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        ) {
            statement.setFetchSize(100);
            try (ResultSet rs = statement.executeQuery(dbDetails.getQuery())) {
                FileWriter writer = new FileWriter(UtilityFunctionsClass.CURRENT_EXECUTION_DIR + File.separator + dbDetails.getEnvironmentAndFileName() + ".csv");
                Class.forName("oracle.jdbc.driver.OracleDriver");

                ResultSetMetaData rsmd = rs.getMetaData();
                int columnNumber = rsmd.getColumnCount();

                for (int i = 1; i <= columnNumber; i++) {
                    writer.append(rsmd.getColumnName(i)).append(",");
                }

                while (rs.next()) {
                    writer.append("\n");
                    for (int i = 1; i <= columnNumber; i++) {
                        if (rs.getString(i) != null) {
                            if (rsmd.getColumnTypeName(i).equalsIgnoreCase("TIMESTAMP")) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS a");
                                Timestamp dbTimestamp = rs.getTimestamp(i);
                                String mainTimeString = dbTimestamp.toString();
                                String replaceWithString = String.format("%0$-9s", mainTimeString.substring(mainTimeString.lastIndexOf(".") + 1).trim())
                                        .replace(" ", "0");
                                mainTimeString = sdf.format(dbTimestamp);
                                String replaceToString = mainTimeString.substring(mainTimeString.lastIndexOf(".") + 1, mainTimeString.lastIndexOf(" ")).trim();
                                writer.append(mainTimeString.replace(replaceToString, replaceWithString)).append(",");
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
                                writer.append(new SimpleDateFormat("dd-MMM-yy").format(rs.getTimestamp(i)));
                            } else {
                                String escapedValue = StringEscapeUtils.escapeCsv(rs.getString(i));
                                writer.append(escapedValue).append(",");
                            }
                        } else if (rs.getString(i) == null) {
                            writer.append(",");
                        }
                    }
                }
                writer.flush();
                writer.close();
            }
            System.out.println("Extraction Completed for= " + dbDetails.getEnvironmentAndFileName());
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
