/**
 * TASK 2. You write this one. About 25 lines.
 *
 * A singly linked list kept in call order at all times.
 *
 * WHAT THIS ROOM PROMISES
 *   Walking from head to tail gives exactly the order the doctor will call
 *   the patients in. So next() is trivial -- the head is the answer.
 *   Everything is paid for in add().
 *
 * THE PART THAT DECIDES YOUR MARK
 *   Patients of the same severity are not interchangeable: the one who
 *   arrived earlier is called earlier. Patient.before(a, b) already knows
 *   the whole rule -- ask it, and never compare severities yourself. Work
 *   out for yourself where the walk has to stop: stop one node too early and
 *   every tie comes out backwards, and your list row stops matching the
 *   given array row.
 *
 * No ready-made collection -- your own Node, a head pointer and a size
 * counter.
 */
public class SortedListRoom implements WaitingRoom {

    private static class Node {
        final Patient p;
        Node next;
        Node(Patient p) { this.p = p; }
    }

    private Node head;
    private int size;

    public void add(Patient p) {
        Node n = new Node(p);
        // Case 1: empty list, or p must be called before the current head.
        // This is the only case that changes head.
        if (head == null || Patient.before(p, head.p)) {
            n.next = head;
            head = n;
        } else {
            // Case 2: walk until the NEXT node exists and does NOT have to go
            // after p -- i.e. keep moving while cur.next must be seen before p.
            // On equal severity Patient.before(cur.next.p, p) is true (earlier id),
            // so p is placed AFTER all its ties and tie order stays by arrival.
            Node cur = head;
            while (cur.next != null && Patient.before(cur.next.p, p)) {
                cur = cur.next;
            }
            n.next = cur.next;
            cur.next = n;
        }
        size++;
    }

    public Patient next() {
        if (head == null) throw new IllegalStateException("empty room");
        Patient p = head.p;
        head = head.next;
        size--;
        return p;
    }

    public int size()        { return size; }
    public boolean isEmpty() { return size == 0; }

    /** GIVEN -- the list from head to tail, used by the trace. Do not change. */
    public String layout() {
        StringBuilder s = new StringBuilder("[");
        for (Node c = head; c != null; c = c.next) {
            if (c != head) s.append(' ');
            s.append('#').append(c.p.id()).append('s').append(c.p.severity());
        }
        return s.append(']').toString();
    }
}