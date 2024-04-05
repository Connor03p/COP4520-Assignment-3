import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App 
{
    public static final int NUMBER_OF_GUESTS = 50000;
    public static final boolean PRINT_EACH_ACTION = false; // Print each action taken by the servants
    public static final boolean CHECK_FOR_ERRORS = false; // Print each list and check for errors. Takes awhile with large # of guests.
    private static final Servant servants[] = {new Servant(),new Servant(),new Servant(),new Servant()};

    public static List<Present> bagOfPresents = Collections.synchronizedList(new ArrayList<Present>());
    public static List<Note> bagOfNotes = Collections.synchronizedList(new ArrayList<Note>());
    public static ConcurrentLinkedList listOfPresents = new ConcurrentLinkedList();
    private static double time;

    public static void main(String[] args) throws Exception 
    {
        // Guests give their presents
        for (int i = 0; i < NUMBER_OF_GUESTS; i++)
        {
            bagOfPresents.add(new Present(i));
        }
        Collections.shuffle(bagOfPresents);

        // Start the timer
        time = System.nanoTime();

        for (int i = 0; i < servants.length; i++)
        {
            servants[i].start();
        }

        for (int i = 0; i < servants.length; i++)
        {
            servants[i].join();
        }

        // Stop the timer
        time = ((System.nanoTime() - time) / 1000000000.0);

        /* 
        if (CHECK_FOR_ERRORS)
        {
            System.out.println("\nBag of Notes: " + bagOfNotes.size());
            for (int i = 0; i < bagOfNotes.size(); i++)
            {
                System.out.println("  Note " + i + ": " + bagOfNotes.get(i).number);
            }

            System.out.println("\nBag of Presents: " + bagOfPresents.size());
            for (int i = 0; i < bagOfPresents.size(); i++)
            {
                System.out.println("  Present " + i + ": " + bagOfPresents.get(i).number);
            }

            System.out.println("\nList of Presents: ");
            ConcurrentNode current = listOfPresents.getFirst();
            while (current != null)
            {
                System.out.println("  Present " + current.data.number);
                current = current.next;
            }

            System.out.println("\nChecking for errors...");

            if (NUMBER_OF_GUESTS != bagOfNotes.size() + bagOfPresents.size())
                System.out.println("The # of guests does not match the # items in each list");
            
            if (!bagOfPresents.isEmpty())
                System.out.println("The bag of presents is not empty");
            
            if (!listOfPresents.isEmpty())
                System.out.println("The list of presents is not empty");
            
            // Check notes were written for all presents
            for (int i = 0; i < NUMBER_OF_GUESTS; i++)
            {
                boolean found = false;
                for (int j = 0; j < bagOfNotes.size(); j++)
                {
                    if (bagOfNotes.get(j).number == i)
                    {
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    System.out.println("  Guest " + i + " was not given a note.");
                }
            }

            // Check for duplicates in any list
            for (int i = 0; i < bagOfNotes.size(); i++)
            {
                for (int j = i + 1; j < bagOfNotes.size(); j++)
                {
                    if (bagOfNotes.get(i).number == bagOfNotes.get(j).number)
                    {
                        System.out.println("  Duplicate note: " + bagOfNotes.get(i).number);
                    }
                }
            }
            for (int i = 0; i < bagOfPresents.size(); i++)
            {
                for (int j = i + 1; j < bagOfPresents.size(); j++)
                {
                    if (bagOfPresents.get(i).number == bagOfPresents.get(j).number)
                    {
                        System.out.println("  Duplicate present in bag: " + bagOfPresents.get(i).number);
                    }
                }
            }
            current = listOfPresents.getFirst();
            while (current != null)
            {
                ConcurrentNode next = current.next;
                while (next != null)
                {
                    if (current.data.number == next.data.number)
                    {
                        System.out.println("  Duplicate present in list: " + current.data.number);
                    }
                    next = next.next;
                }
                current = current.next;
            }
        }
        
        System.out.println("\nFinished in " + time + " seconds.");
    */
    }
}
