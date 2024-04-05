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
        if (!App.bagOfPresents.isEmpty())
        {
            presentFromBag = App.bagOfPresents.remove(0);
        }

        // Add the present to the chain
        if (presentFromBag != null)
        {
            App.listOfPresents.add(presentFromBag.number);
        }
    }

    public void writeNote()
    {
        // Unlink the gift from its predecessor
        Present presentFromList = App.listOfPresents.removeFirst();

        // Write a “Thank you” card to a guest
        if (presentFromList != null)
        {
            App.bagOfNotes.add(new Note(presentFromList.number));
        }   
    }

    public void checkPresent()
    {
        int presentIndex = random.nextInt(App.NUMBER_OF_GUESTS);
        App.listOfPresents.contains(presentIndex);
    }
}
