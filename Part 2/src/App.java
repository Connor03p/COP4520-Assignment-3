/**
    You are tasked with the design of the module responsible for measuring the atmospheric
    temperature of the next generation Mars Rover, equipped with a multicore CPU and 8
    temperature sensors. The sensors are responsible for collecting temperature readings at
    regular intervals and storing them in shared memory space. The atmospheric
    temperature module has to compile a report at the end of every hour, comprising the top
    5 highest temperatures recorded for that hour, the top 5 lowest temperatures recorded
    for that hour, and the 10-minute interval of time when the largest temperature
    difference was observed. The data storage and retrieval of the shared memory region
    must be carefully handled, as we do not want to delay a sensor and miss the interval of
    time when it is supposed to conduct temperature reading. Design and implement a
    solution using 8 threads that will offer a solution for this task. Assume that the
    temperature readings are taken every 1 minute. In your solution, simulate the operation
    of the temperature reading sensor by generating a random number from -100F to 70F at
    every reading.
 */
import java.util.Comparator;
import java.util.concurrent.*;

public class App 
{
    public static final long timeMultiplier = 600;
    public static final long sensorInterval = 1000 * 60;
    public static final long reportInterval = 1000 * 60 * 60;
    public static int numReports = 1;

    private final ScheduledExecutorService executor;
    private final PriorityBlockingQueue<Task> queue;

    public App() {
        executor = Executors.newScheduledThreadPool(10); // Example: 5 threads
        queue = new PriorityBlockingQueue<>(10, Comparator.comparingLong(task -> {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastExecution = currentTime - task.getLastExecutionTime();
            return Math.max(0, task.getInterval() - timeSinceLastExecution);
        }));
    }

    public void scheduleTask(Task task) {
        queue.offer(task);
    }

    public void start() 
    {
        int numTasks = queue.size();
        for (int i = 0; i < numTasks; i++) // Example: 5 threads
        {
            Task task;
            try
            {
                task = queue.take(); // Take the highest priority task from the queue
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                System.err.println("Scheduler Interrupted");
                return;
            }

            executor.schedule(() -> {
                task.run();
                Task newTask = task instanceof Sensor ? new Sensor() : new Report();
                queue.offer(newTask); // Reschedule the task
                executor.schedule(this::start, newTask.getInterval(), TimeUnit.MILLISECONDS); // Reschedule the start method
            }, task.getInterval(), TimeUnit.MILLISECONDS);
        }
    }

    public static void main(String[] args) {
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
        
        scheduler.start();
    }
}