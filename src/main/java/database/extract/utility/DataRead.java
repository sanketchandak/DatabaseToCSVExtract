package database.extract.utility;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataRead {
    static String inputFilePath = System.getProperty("user.dir") + File.separator + "inputData" + File.separator + "DatabaseDetails.xlsx";
    private static List<Map<String, String>> dbDetails = new ArrayList<>();
    private static Map<String, String> extractSetupDetails = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static List<Integer> getExecutionScheduledTime() {
        List<Integer> timeDetails = new ArrayList<>();
        setExecutionSetupDetails();
        timeDetails.add(Integer.parseInt(extractSetupDetails.get("ExecutionHour")));
        timeDetails.add(Integer.parseInt(extractSetupDetails.get("ExecutionMinutes")));
        timeDetails.add(Integer.parseInt(extractSetupDetails.get("ExecutionSeconds")));
        return timeDetails;
    }

    public static int getThreadCount() {
        setExecutionSetupDetails();
        return Integer.parseInt(extractSetupDetails.get("ParallelThreads"));
    }

    static String getExtractionDirectoryPath() {
        setExecutionSetupDetails();
        return extractSetupDetails.get("ExtractionDirectoryPath");
    }

    private static void setExecutionSetupDetails() {
        if(extractSetupDetails.isEmpty()){
            try {
                Fillo fillo = new Fillo();
                Connection connection = fillo.getConnection(inputFilePath);
                String extractSetupDetailsQuery = "SELECT * FROM ExtractSetup";
                Recordset extractSetupRecordset = connection.executeQuery(extractSetupDetailsQuery);
                List<String> dbDetailsColumnNames = extractSetupRecordset.getFieldNames();
                while (extractSetupRecordset.next()) {
                    for (String columnName : dbDetailsColumnNames) {
                        extractSetupDetails.put(columnName.trim(), extractSetupRecordset.getField(columnName));
                    }
                }
            } catch (FilloException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Map<String, String>> getDBDetailsForExtract() {
        if (dbDetails.isEmpty()) {
            try {
                Fillo fillo = new Fillo();
                Connection connection = fillo.getConnection(inputFilePath);
                String dbDetailsQuery = "SELECT * FROM DatabaseDetails WHERE ExecutionFlag like 'Y%'";
                Recordset dbDetailsRecordset = connection.executeQuery(dbDetailsQuery);
                List<String> dbDetailsColumnNames = dbDetailsRecordset.getFieldNames();
                while (dbDetailsRecordset.next()) {
                    Map<String, String> rowDbDetails = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    for (String columnName : dbDetailsColumnNames) {
                        rowDbDetails.put(columnName.trim(), dbDetailsRecordset.getField(columnName));
                    }
                    dbDetails.add(rowDbDetails);
                }
            } catch (FilloException e) {
                e.printStackTrace();
            }
        }
        return dbDetails;
    }

}
