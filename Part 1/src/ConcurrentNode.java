import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentNode 
{
    public Present data;
    public ConcurrentNode next;
    public ConcurrentNode prev;
    public ReentrantLock lock = new ReentrantLock();
    public boolean marked;
    public int key;
    
    public ConcurrentNode(Present data)
    {
        this.data = data;
        this.next = null;
        this.key = this.hashCode();
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