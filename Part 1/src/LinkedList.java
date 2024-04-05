public class LinkedList 
{
    private Node head;
    public int size;
    
    public LinkedList()
    {
        head = null;
        size = 0;
    }

    public boolean isEmpty()
    {
        return head == null;
    }

    public void addFirst(Present data)
    {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    public Node getFirst()
    {
        if (head == null)
        {
            return null;
        }
        return head;
    }
    
    public void add(Present data)
    {
        // Add to list, keeping presents in numerical order
        Node newNode = new Node(data);
        if (head == null)
        {
            head = newNode;
        }
        else if (head.data.number > data.number)
        {
            newNode.next = head;
            head = newNode;
        }
        else
        {
            Node current = head;
            while (current.next != null && current.next.data.number < data.number)
            {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    public Node get(int index)
    {
        if (index < 0 || index >= size)
        {
            return null;
        }
        Node current = head;
        for (int i = 0; i < index; i++)
        {
            current = current.next;
        }
        return current;
    }

    public boolean contains(Present data)
    {
        Node current = head;
        while (current != null)
        {
            if (current.data.equals(data))
            {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int indexOf(Present data)
    {
        Node current = head;
        for (int i = 0; i < size; i++)
        {
            if (current.data.equals(data))
            {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    public Node remove(Present data)
    {
        int index = indexOf(data);
        Node result = null;
        if (index != -1)
        {
            result = remove(index);
        }
        return result;
    }

    public Node remove (int index)
    {
        if (index < 0 || index >= size)
        {
            return null;
        }
        Node result = null;
        if (index == 0)
        {
            result = head;
            head = head.next;
        }
        else
        {
            Node current = head;
            for (int i = 0; i < index - 1; i++)
            {
                current = current.next;
            }
            result = current.next;
            current.next = current.next.next;
        }
        size--;
        return result;
    }
}
