import java.util.Random;

public class Reading 
{
    public final int value;
    public final long time;

    final Random rand = new Random();

    public Reading(long time)
    {
        value = rand.nextInt(170) - 170;
        this.time = time;
    }
}
