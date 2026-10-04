/**
 * TASK 1 -- singly linked queue, no triage. FIFO: severity is ignored.
 * add = link onto tail, next = unlink head. Both O(1).
 */
public class FifoQueueRoom implements WaitingRoom {

    private static class Node {
        final Patient p;
        Node next;
        Node(Patient p) { this.p = p; }
    }

    private Node head, tail;
    private int size;

    public void add(Patient p) {
        Node n = new Node(p);
        if (tail == null) {          // empty room: new node is both head and tail
            head = n;
            tail = n;
        } else {
            tail.next = n;           // link after the current tail
            tail = n;                // and it becomes the new tail
        }
        size++;
    }

    public Patient next() {
        if (head == null) throw new IllegalStateException("empty room");
        Patient p = head.p;
        head = head.next;
        if (head == null) tail = null;   // queue became empty: tail must not dangle
        size--;
        return p;
    }

    public int size()        { return size; }
    public boolean isEmpty() { return size == 0; }
}