package database.extract.executerservice;

import database.extract.utility.DataRead;
import database.extract.utility.DataWrite;
import database.extract.utility.UtilityFunctionsClass;
import database.extract.utility.WorkerThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class ThreadRunner {
    private static final List<Map<String, String>> EXECUTE_DBDETAILS_LIST = DataRead.getDBDetailsForExtract();
    private static final int THREAD_COUNT = DataRead.getThreadCount();

    void threadSplitterRunner() throws ExecutionException, InterruptedException {
        System.out.println("---------------------Execution Started---------------------");
        List<Future<?>> futures = new ArrayList<Future<?>>();
        UtilityFunctionsClass.createDirectoryForReports();
        ExecutorService executorService = Executors.newFixedThreadPool(ThreadRunner.THREAD_COUNT);
        for(Map<String, String> dbDetails : ThreadRunner.EXECUTE_DBDETAILS_LIST){
            Runnable runnable = new WorkerThread(dbDetails);
            Future<?> f = executorService.submit(runnable);
            futures.add(f);
        }
        executorService.shutdown();

        for (Future<?> future : futures){
            future.get();
        }

        boolean allDone = false;
        for (Future<?> future : futures){
            allDone = future.isDone();
            if(!allDone){
                break;
            }
        }
        if (allDone){
            System.out.println("---------------------Execution Finished---------------------");
            DataWrite.setExtractResultPath();
        }
    }
}
