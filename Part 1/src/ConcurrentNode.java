import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentNode 
{
    public Present data;
    public ConcurrentNode next = null;
    public ConcurrentNode prev = null;
    public ReentrantLock lock = new ReentrantLock();
    public boolean marked = false;
    public int key;
    
    public ConcurrentNode(Present data)
    {
        this.data = data;
        this.next = null;
        this.key = data.number;
    }

    public void lock()
    {
        lock.lock();
    }

    public void unlock()
    {
        lock.unlock();
    }
}