import java.util.ArrayList;
import java.util.List;

class Report extends Task
{
    public Report() {
        super((long)(App.reportInterval / App.timeMultiplier), "Report");
    }

    @Override
    public void run()
    {
        System.out.println("Compiling Report...");
        List<Reading> readingsCopy = SharedMem.copy();
        List<Reading> highest = new ArrayList<Reading>();
        List<Reading> lowest = new ArrayList<Reading>();

        int reportHighest = 5;
        int reportLowest = 5;
        long differenceInterval = 10;
        
        differenceInterval = ((1000 * 60 * differenceInterval) / App.timeMultiplier);

        if (readingsCopy.size() < (reportHighest + reportLowest))
        {
            System.out.println("Not enough readings to compile report");
            return;
        }

        for (int i = 0; i < reportHighest; i++)
        {
            Reading max = readingsCopy.get(0);
            for (int j = 0; j < readingsCopy.size(); j++)
            {
                Reading reading = readingsCopy.get(j);
                if (reading.value > max.value)
                {
                    max = reading;
                }
            }
            highest.add(max);
            readingsCopy.remove(max);
        }

        for (int i = 0; i < reportLowest; i++)
        {
            Reading min = readingsCopy.get(0);
            for (int j = 0; j < readingsCopy.size(); j++)
            {
                Reading reading = readingsCopy.get(j);
                if (reading.value < min.value)
                {
                    min = reading;
                }
            }
            lowest.add(min);
            readingsCopy.remove(min);
        }

        String output = "\nReport Finished:";
        output += ("\n  Highest: ");
        for (int i = 0; i < highest.size(); i++)
        {
            Reading reading = highest.get(i);
            output += reading.value + ", ";
        }

        output += "\n  Lowest: ";
        for (int i = 0; i < lowest.size(); i++)
        {
            Reading reading = lowest.get(i);
            output += reading.value + ", ";
        }

        // Find the greatest difference within a 10 minute interval
        Reading maxDiff = new Reading(0);
        Reading minDiff = new Reading(0);
        for (int i = 0; i < readingsCopy.size(); i++)
        {
            for (int j = 0; j < readingsCopy.size(); j++)
            {
                Reading reading1 = readingsCopy.get(i);
                Reading reading2 = readingsCopy.get(j);

                // Get the time difference between the two readings
                long timeDiff = Math.abs(reading1.time - reading2.time);

                // Ensure the time difference is within the interval (10 minutes)
                if (timeDiff > differenceInterval) continue;

                // Check if the value difference is the greatest
                if (Math.abs(reading1.value - reading2.value) > Math.abs(maxDiff.value - minDiff.value))
                {
                    maxDiff = reading1;
                    minDiff = reading2;
                }
            }
        }
        
        output += "\n  Largest difference in a 10 minute interval (" + differenceInterval + "ms) was between " + maxDiff.value + " (at " + maxDiff.time + "ms) and " + minDiff.value + " (at " + minDiff.time + "ms)\n";
        System.out.println(output);

        App.numReports--;
        if (App.numReports == 0)
        {
            System.out.println("All reports compiled");
            System.out.println("Finished in " + (System.currentTimeMillis() - App.startTime) + " ms");
            System.exit(0);
        }

        // Clear the readings
        SharedMem.clear();
    }
}