package database.extract.utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityFunctionsClass {

    private static final String CURRENT_EXECUTION_TIME_FOLDER = getTimeStamp();
    private static final String CURRENT_EXECUTION_MAIN_DIR = DataRead.getExtractionDirectoryPath() + File.separatorChar;
    static final String CURRENT_EXECUTION_DIR = CURRENT_EXECUTION_MAIN_DIR + CURRENT_EXECUTION_TIME_FOLDER;

    public static void createDirectoryForReports() {
        createDir(CURRENT_EXECUTION_MAIN_DIR);
        createDir(CURRENT_EXECUTION_DIR);
    }

    private static String getTimeStamp(){
        return timeStamp();
    }

    private static String timeStamp() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");
        return dateFormat.format(date);
    }

    private static void createDir(String dirNameToCreate){
        File theDir = new File(dirNameToCreate);
        if(!theDir.exists()){
            try {
                theDir.mkdir();
            }
            catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }
}
