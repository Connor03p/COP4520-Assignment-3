import java.util.Random;

public class Servant extends Thread
{
    final Random random = new Random();
    
    public void run() 
    {
        while (App.bagOfNotes.size() < App.NUMBER_OF_GUESTS)
        {
            randomAction();
        }
    }

    public void randomAction()
    {
        int action = random.nextInt(3);
        switch (action)
        {
            case 0:
                addPresent();
                break;
            case 1:
                writeNote();
                break;
            case 2:
                checkPresent();
                break;
        }
    }

    public void addPresent()
    {
        Present presentFromBag = null;

        // Take a present from the bag
        synchronized (App.bagOfPresents)
        {
            if (App.bagOfPresents.size() > 0)
            {
                presentFromBag = App.bagOfPresents.remove(0);
            }
        }

        // Add the present to the chain
        if (presentFromBag != null)
        {
            synchronized (App.listOfPresents)
            {
                App.listOfPresents.add(presentFromBag);
            }
        }
    }

    public void writeNote()
    {
        Node presentFromList = null;

        // Unlink the gift from its predecessor
        synchronized (App.listOfPresents)
        {
            if (App.listOfPresents.size > 0)
            {
                presentFromList = App.listOfPresents.remove(0);
            }
        }

        // Write a “Thank you” card to a guest
        if (presentFromList != null)
        {
            synchronized (App.bagOfNotes)
            {
                App.bagOfNotes.add(new Note(presentFromList.data.number));
            }
        }   
    }

    public void checkPresent()
    {
        if (App.listOfPresents.size == 0)
            return;
        
        int presentIndex = random.nextInt(App.NUMBER_OF_GUESTS);

        // Check whether a gift with a particular tag was present in the chain or not
        synchronized (App.listOfPresents)
        {
            if (App.listOfPresents.size > 0)
            {
                App.listOfPresents.contains(new Present(presentIndex));
            }
        }
    }
}
