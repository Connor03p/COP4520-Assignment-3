import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

class SharedMem
{
    private static ConcurrentLinkedQueue<Reading> readings = new ConcurrentLinkedQueue<>();

    public static void addReading(Reading reading)
    {
        readings.add(reading);
    }

    public static List<Reading> copy()
    {
        List<Reading> copy = new ArrayList<Reading>(readings);
        return copy;
    }

    public static synchronized int size()
    {
        return readings.size();
    }

    public static synchronized void clear()
    {
        readings.clear();
    }
}