abstract class Task implements Runnable
{
    protected String name;
    protected final long interval;
    protected long lastExecutionTime;
    protected long intervalOffset = 0;

    public Task(long interval, String name) {
        this.name = name;
        this.interval = interval;
        this.lastExecutionTime = System.currentTimeMillis();
    }

    abstract public void run();

    protected void checkIntervalOffset()
    {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastExecution = currentTime - lastExecutionTime;
        lastExecutionTime = System.currentTimeMillis();
        intervalOffset = timeSinceLastExecution;

        // Check if the task is running behind
        if (intervalOffset > interval * 2)
        {
            System.out.println(name + " missed an interval behind by " + Math.abs(intervalOffset - interval) + " ms (Interval: " + interval + " ms, Offset: " + intervalOffset + " ms)");
            if (App.STOP_IF_RUNNING_BEHIND)
            {
                System.out.println("Stopping...");
                System.exit(0);
            }
        }
    }

    public long getInterval() {
        return interval;
    }

    public long getLastExecutionTime() {
        return lastExecutionTime;
    }

}