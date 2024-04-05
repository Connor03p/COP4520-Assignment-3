import java.util.ArrayList;
import java.util.List;

class SharedMem
{
    private static List<Reading> readings = new ArrayList<Reading>();

    public static synchronized void addReading(Reading reading)
    {
        readings.add(reading);
    }

    public static synchronized List<Reading> copy()
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