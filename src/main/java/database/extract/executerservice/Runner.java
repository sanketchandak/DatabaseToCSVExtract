package database.extract.executerservice;

import database.extract.utility.DataRead;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class Runner extends TimerTask {

    @Override
    public void run() {
        try {
            new ThreadRunner().threadSplitterRunner();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static Date verifySchedulingTime() {
        Date dateCurrentTime = new Date();
        Calendar dateExecutionTimeCalendar = Calendar.getInstance();
        dateExecutionTimeCalendar.set(Calendar.HOUR, DataRead.getExecutionHour());
        dateExecutionTimeCalendar.set(Calendar.MINUTE, DataRead.getExecutionMinutes());
        dateExecutionTimeCalendar.set(Calendar.SECOND, DataRead.getExecutionSeconds());
        Date dateExecutionTime = dateExecutionTimeCalendar.getTime();
        if (dateExecutionTime.before(dateCurrentTime)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateExecutionTime);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dateExecutionTime = calendar.getTime();
        }
        System.out.println("Scheduled Time For Export is: " + dateExecutionTime);
        return dateExecutionTime;
    }

    private static void StartTask() {
        Runner task = new Runner();
        Timer timer = new Timer();
        timer.schedule(task, verifySchedulingTime());
    }

    public static void main(String[] args) {
        StartTask();
    }
}
