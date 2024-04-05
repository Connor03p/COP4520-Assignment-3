public class ConcurrentLinkedList {
    private ConcurrentNode head;
    private int size;

    public ConcurrentLinkedList() {
        head = new ConcurrentNode(new Present(Integer.MIN_VALUE));
        head.next = new ConcurrentNode(new Present(Integer.MAX_VALUE));
        size = 0;
    }

    public boolean isEmpty() {
        return head.next.key == Integer.MAX_VALUE;
    }

    public ConcurrentNode getFirst() {
        return head;
    }

    private boolean validate(ConcurrentNode pred, ConcurrentNode curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }

    public boolean add(int key) {
        while (true) {
            ConcurrentNode pred = head;
            ConcurrentNode curr = pred.next;
            while (curr.key <= key) {
                pred = curr;
                curr = curr.next;
            }
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    if (curr.key != key) {
                        ConcurrentNode node = new ConcurrentNode(new Present(key));
                        node.next = curr;
                        pred.next = node;
                        if (App.PRINT_EACH_ACTION)
                            System.out.println("Added " + node.key + " to the list");
                        return true;
                    }
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public Present removeFirst()
    {
        ConcurrentNode pred = head;
        ConcurrentNode curr = pred.next;
        pred.lock();
        curr.lock();
        try
        {
            if (validate(pred, curr))
            {
                if (curr.key != Integer.MAX_VALUE)
                {
                    curr.marked = true;
                    Present output = curr.data;
                    pred.next = curr.next;
                    size--;
                    if (App.PRINT_EACH_ACTION)
                        System.out.println("Removed " + output.number + " from the list");
                    return output;
                }
            }
        }
        finally
        {
            pred.unlock();
            curr.unlock();
        }
        return null;
    }

    public Present get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        ConcurrentNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public boolean contains(int key) 
    {
        while (true) {
            ConcurrentNode pred = head; 
            ConcurrentNode curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            try {
                pred.lock();
                curr.lock();
                if (validate(pred, curr)) {
                    if (curr.key == key)
                    {
                        if (App.PRINT_EACH_ACTION)
                            System.out.println("List contains " + key);
                        return true;
                    }
                    else
                    {
                        if (App.PRINT_EACH_ACTION)
                            System.out.println("List does not contain " + key);
                        return false;
                    }
                }
            } finally { // always unlock
                pred.unlock();
                curr.unlock();
            }
        }
    }
}