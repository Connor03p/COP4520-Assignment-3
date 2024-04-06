import java.util.concurrent.locks.ReentrantLock;

public class Node 
{
    public Present data;
    public Node next = null;
    public Node prev = null;
    public ReentrantLock lock = new ReentrantLock();
    public boolean marked = false;
    public int key;
    
    public Node(Present data)
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