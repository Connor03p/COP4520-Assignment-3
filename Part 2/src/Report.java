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
        int differenceInterval = 10;

        if (readingsCopy.size() < (reportHighest + reportLowest))
        {
            System.out.println("Not enough readings to compile report");
            return;
        }

        for (int i = 0; i < reportHighest; i++)
        {
            Reading max = readingsCopy.get(0);
            for (Reading reading : readingsCopy)
            {
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
            for (Reading reading : readingsCopy)
            {
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
        for (Reading reading : highest)
        {
            output += reading.value + ", ";
        }

        output += "\n  Lowest: ";
        for (Reading reading : lowest)
        {
            output += reading.value + ", ";
        }

        // Find the largest temperature difference taken within reportDifferenceInterval
        Reading maxDiff = readingsCopy.get(0);
        Reading minDiff = readingsCopy.get(0);
        for (int i = 0; i < readingsCopy.size(); i++)
        {
            for (int j = i + 1; j < readingsCopy.size(); j++)
            {
                Reading reading1 = readingsCopy.get(i);
                Reading reading2 = readingsCopy.get(j);
                if (Math.abs(reading1.value - reading2.value) > Math.abs(maxDiff.value - minDiff.value))
                {
                    maxDiff = reading1;
                    minDiff = reading2;
                }
            }
        }
        output += "\n  Largest difference in a " + differenceInterval + " minute interval was between " + maxDiff.value + " and " + minDiff.value + "\n";
        System.out.println(output);

        App.numReports--;
        if (App.numReports == 0)
        {
            System.out.println("All reports compiled");
            System.exit(0);
        }

        // Clear the readings
        SharedMem.clear();
    }
}