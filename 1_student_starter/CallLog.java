import java.util.Arrays;
import java.util.List;

/**
 * TASK 4 -- stack backed by our own growing array (doubles when full).
 * push/pop/peek are O(1) (push amortised); recent(n) is O(n) and read-only.
 */
public class CallLog {

    public static final class Entry {
        public final Patient patient;
        public final int minute;
        public Entry(Patient patient, int minute) { this.patient = patient; this.minute = minute; }
    }

    private Entry[] a = new Entry[4];
    private int size;

    public void push(Entry e) {
        if (size == a.length) {                       // full: double
            Entry[] bigger = new Entry[a.length * 2];
            System.arraycopy(a, 0, bigger, 0, size);
            a = bigger;
        }
        a[size] = e;
        size++;
    }

    public Entry pop() {
        if (size == 0) return null;
        size--;
        Entry top = a[size];
        a[size] = null;
        return top;
    }

    public Entry peek() {
        if (size == 0) return null;
        return a[size - 1];
    }

    public int size()        { return size; }
    public boolean isEmpty() { return size == 0; }

    /** The n most recently called patients, most recent first. Read-only: never pops. */
    public List<Entry> recent(int n) {
        if (n < 0) n = 0;
        if (n > size) n = size;
        Entry[] out = new Entry[n];
        for (int i = 0; i < n; i++) {
            out[i] = a[size - 1 - i];     // top of the stack first
        }
        return Arrays.asList(out);        // fixed-size view over a fresh array copy; stack untouched
    }
}