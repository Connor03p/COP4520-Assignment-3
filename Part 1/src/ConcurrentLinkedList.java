public class ConcurrentLinkedList {
    private Node head;

    public ConcurrentLinkedList() {
        head = new Node(new Present(Integer.MIN_VALUE));
        head.next = new Node(new Present(Integer.MAX_VALUE));
    }

    public boolean isEmpty() {
        return head.next.key == Integer.MAX_VALUE;
    }

    public Node getHead() {
        return head;
    }

    private boolean validate(Node pred, Node curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }

    public boolean add(int key) {
        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key <= key) {
                pred = curr;
                curr = curr.next;
            }
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    if (curr.key != key) {
                        Node node = new Node(new Present(key));
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
        Node pred = head;
        Node curr = pred.next;
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

    public boolean contains(int key) 
    {
        while (true) {
            Node pred = head; 
            Node curr = pred.next;
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