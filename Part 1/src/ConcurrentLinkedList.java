public class ConcurrentLinkedList {
    private ConcurrentNode head;
    private int size;

    public ConcurrentLinkedList() {
        head = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private boolean validate(ConcurrentNode pred, ConcurrentNode curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }

    public boolean add(Present item) {
        int key = item.hashCode();
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
                    if (curr.key == key) {
                        return false;
                    } else {
                        ConcurrentNode node = new ConcurrentNode(item);
                        node.next = curr;
                        pred.next = node;
                        return true;
                    }
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean remove(Present item) {
        int key = item.hashCode();
        while (true) {
            ConcurrentNode pred = head;
            ConcurrentNode curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    if (curr.key == key) {
                        pred.next = curr.next;
                        return true;
                    } else {
                        return false;
                    }
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean contains(Present item) {
        int key = item.hashCode();
        while (true) {
            ConcurrentNode pred = this.head; // sentinel node;
            ConcurrentNode curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            try {
                pred.lock();
                curr.lock();
                if (validate(pred, curr)) {
                    return (curr.key == key);
                }
            } finally { // always unlock
                pred.unlock();
                curr.unlock();
            }
        }
    }
}