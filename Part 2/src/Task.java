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
        intervalOffset = interval - timeSinceLastExecution;
        if (intervalOffset > interval) {
            System.out.println("Task missed its interval by " + intervalOffset + "ms");
        }
    }

    public long getInterval() {
        return interval;
    }

    public long getLastExecutionTime() {
        return lastExecutionTime;
    }

}