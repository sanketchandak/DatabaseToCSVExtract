package database.extract.utility;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;

public class DataWrite {
    public static void setExtractResultPath() {
        try {
            Fillo fillo = new Fillo();
            Connection connection = fillo.getConnection(DataRead.inputFilePath);
            String extractSetupDetailsQuery = "Update ExtractSetup Set ExtractionGeneratedPath='"+UtilityFunctionsClass.CURRENT_EXECUTION_DIR+"'";
            connection.executeQuery(extractSetupDetailsQuery);
            connection.close();
        } catch (FilloException e) {
            e.printStackTrace();
        }
    }
}
