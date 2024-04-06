import java.util.Comparator;
import java.util.concurrent.*;

public class App 
{
    public static final long timeMultiplier = 2400;
    public static final long sensorInterval = 1000 * 60;
    public static final long reportInterval = 1000 * 60 * 60;
    public static final boolean STOP_IF_RUNNING_BEHIND = false;
    public static int numReports = 1;

    // May require reduced time multiplier
    public static final boolean PRINT_SCHEDULE = false;
    public static final boolean PRINT_READINGS = false; 
    

    private final ScheduledExecutorService executor;
    private final PriorityBlockingQueue<Task> queue;

    public static long startTime = System.currentTimeMillis();

    public App() {
        executor = Executors.newScheduledThreadPool(9);
        queue = new PriorityBlockingQueue<>(9, Comparator.comparingLong(task -> {
            // Prioritize tasks that are behind
            return Math.max(0, Math.min(task.interval - task.intervalOffset, task.interval));
        }));
    }

    public void scheduleTask(Task task) {
        queue.offer(task);
    }

    public void start() 
    {
        int numTasks = queue.size();
        for (int i = 0; i < numTasks; i++)
        {
            Task task;
            try
            {
                task = queue.take();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                System.err.println("Scheduler Interrupted");
                return;
            }

            // Calculate the delay based on how far behind the task is
            long delay = Math.max(0, Math.min(task.interval - task.intervalOffset, task.interval));
            
            if (PRINT_SCHEDULE)
                System.out.println("Starting " + task.name + " in " + delay + " ms");

            executor.schedule(() -> {

                // Run the task
                task.run();

                // Add the task back to the queue
                queue.offer(task);

                // Reschedule the start method, which will take the task from the queue
                executor.schedule(this::start, delay, TimeUnit.MILLISECONDS);

            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting...");
        App scheduler = new App();
        scheduler.scheduleTask(new Report());
        scheduler.scheduleTask(new Sensor());
        scheduler.scheduleTask(new Sensor());
        scheduler.scheduleTask(new Sensor());
        scheduler.scheduleTask(new Sensor());
        scheduler.scheduleTask(new Sensor());
        scheduler.scheduleTask(new Sensor());
        scheduler.scheduleTask(new Sensor());
        scheduler.scheduleTask(new Sensor());
        
        startTime = System.currentTimeMillis();
        scheduler.start();
    }
}