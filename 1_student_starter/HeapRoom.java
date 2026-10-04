/**
 * TASK 3. You write this one. About 45 lines. The hardest of the four --
 * do it after SortedListRoom is passing.
 *
 * A binary max-heap stored in a plain array -- no node objects, no pointers.
 * For index i: parent is (i-1)/2, children are 2i+1 and 2i+2.
 * "Max" here means "should be called next", under the same rule as
 * SortedListRoom: Patient.before(a, b) decides that, same as before -- never
 * compare severities yourself.
 *
 * WHAT THIS ROOM PROMISES
 *   The root (index 0) is always the patient who should be called next.
 *   add() puts a new patient in and restores that promise by sifting up;
 *   next() removes the root and restores the promise by sifting down.
 *
 * THE PART THAT DECIDES YOUR MARK
 *   siftDown must compare BOTH children, not just one -- pick whichever
 *   child is "more before" than the current node (if either), swap with
 *   that one, and keep going. Comparing only the left child is a bug that a
 *   short hand-trace will often NOT catch -- it only shows up once the heap
 *   is several levels deep, which a full shift (java Main, not just
 *   `trace`) will expose.
 *
 * No ready-made collection -- your own Patient[] array, resized by doubling
 * when full, exactly like ArrayRoom does.
 */
public class HeapRoom implements WaitingRoom {

    private Patient[] a = new Patient[4];
    private int size;

    public void add(Patient p) {
        if (size == a.length) {                       // full: double the array
            Patient[] bigger = new Patient[a.length * 2];
            System.arraycopy(a, 0, bigger, 0, size);
            a = bigger;
        }
        a[size] = p;
        siftUp(size);
        size++;
    }

    public Patient next() {
        if (size == 0) throw new IllegalStateException("empty room");
        Patient top = a[0];
        size--;
        a[0] = a[size];       // last element goes to the root
        a[size] = null;       // clear the unused slot
        if (size > 0) siftDown(0);
        return top;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (Patient.before(a[i], a[parent])) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1, right = 2 * i + 2;
            int best = i;
            if (left < size && Patient.before(a[left], a[best]))   best = left;
            if (right < size && Patient.before(a[right], a[best])) best = right;
            if (best == i) return;     // heap property holds
            swap(i, best);
            i = best;
        }
    }

    private void swap(int i, int j) { Patient t = a[i]; a[i] = a[j]; a[j] = t; }

    public int size()        { return size; }
    public boolean isEmpty() { return size == 0; }

    /** GIVEN -- the heap array from index 0, used by the trace. Do not change. */
    public String layout() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) s.append(' ');
            s.append('#').append(a[i].id()).append('s').append(a[i].severity());
        }
        return s.append(']').toString();
    }
}