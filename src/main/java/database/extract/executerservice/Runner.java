package database.extract.executerservice;

import database.extract.utility.DataRead;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class Runner extends TimerTask {

    private final static List<Integer> TIME_DETAILS = DataRead.getExecutionScheduledTime();

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
        dateExecutionTimeCalendar.set(Calendar.HOUR, TIME_DETAILS.get(0));
        dateExecutionTimeCalendar.set(Calendar.MILLISECOND, TIME_DETAILS.get(1));
        dateExecutionTimeCalendar.set(Calendar.SECOND, TIME_DETAILS.get(2));
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
