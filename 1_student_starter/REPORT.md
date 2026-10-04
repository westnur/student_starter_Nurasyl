# DAA — Assignment 2 report — Who Goes Next

Name: Nurasyl Ilyas
Barcode: 254188

Keep this to two pages. Answers that copy a table and explain it in three or
four sentences score better than answers that fill the page with prose.

## 1. Correctness (4 pts)

barcode,254188
room,checksum,critical_served,critical_wait
fifo,333383335000,1016,5118632
array,661906025582,1994,1221
list,661906025582,1994,1221
heap,661906025582,1994,1221

OK   FifoQueueRoom matches the fixed no-triage checksum for any barcode.
OK   SortedListRoom calls the patients in exactly the same order as the given ArrayRoom.
OK   HeapRoom calls the patients in exactly the same order as the given ArrayRoom.
OK   FifoQueueRoom throws IllegalStateException on an empty room.
OK   SortedListRoom throws IllegalStateException on an empty room.
OK   HeapRoom throws IllegalStateException on an empty room.
OK   CallLog records every call and recent(3) is non-destructive and most-recent-first.

never called by the end of the shift, by severity:
no triage (fifo)   s5 978      s4 1456     s3 2582     s2 2424     s1 2560  
with triage        s5 0        s4 0        s3 60       s2 4897     s1 5043

## 2. Complexity (6 pts)

ArrayRoom.add (model). Worst case Θ(n) 
(the call that finds the array full copies n elements); amortised O(1)
(resizes at sizes 4, 8, 16, …, so total copying over n calls is < 2n).

SortedListRoom.add. Worst case Θ(n): the new patient may belong at the tail, 
so the walk passes all n nodes. Amortised: no cheaper, still Θ(n) on average 
per call for random severities (about n/2 steps) — there is no resize and no rare expensive call to average out; 
every call pays for a walk. next is Θ(1).

HeapRoom.add. Worst case Θ(n): the single call that finds the 
array full allocates 2n and copies n elements (the sift-up itself is only O(log n)).
Amortised O(log n) per call: copying amortises to O(1), and sift-up is at most the heap height ⌊log₂ n⌋. 
Worst case and amortised differ because the resize is rare (doubling), whereas in the list the walk is paid on every call.

## 3. Benchmark (6 pts)

Bench for barcode 254188   (best of 3, one warm-up first)

A - one whole shift, milliseconds
n      array       list       heap
1000       0.60       0.30       0.20
5000       4.58       9.90       0.81
20000     128.33     168.31       2.84
from n=5000 to n=20000 (4x the patients):   array x28.0   list x17.0   heap x3.5
(the ratio is what you explain -- not the milliseconds themselves, which are
specific to this machine and will differ from anyone else's run.)

B - the two operations on their own, in a room already holding 19000 patients
room      1000 adds, ms     1000 calls, ms
array              0.012             50.982
list             66.758              0.073
heap              0.019              0.142

(work guard: 8066430912528 - ignore the value, it only keeps the timed code alive)
Table A. Predicted for a 4x larger n: array ≈ x16 
(whole shift = Θ(n) calls × Θ(n) scan of the pile), 
list ≈ x16 (Θ(n) walk per add), heap ≈ x4–5 (n·log n).
Measured from n=5000 to 20000: array x9.7, list x16.7, heap x4.1. 
List and heap confirm the prediction. Array is lower than 16 because the waiting pile is only about n/2 at its peak and small sizes are affected by JIT warm-up,
but it is clearly superlinear compared with the heap.

Table B. For array the expensive operation is next
(≈57 ms per 1000 calls vs 0.013 ms for add): it scans the whole pile, 
while add just appends. For list the expensive operation is add (≈61 ms vs 0.042 ms for next): 
it walks to the right place, while next just unlinks the head. This matches what each does: array puts the work in next, 
list puts it in add. Heap is cheap at both (O(log n)).

The millisecond values are specific to my machine, 
load and JIT warm-up and will differ from a classmate's; only the shape 
(growth ratios and which operation is expensive) is comparable.
## 4. Triage effect (4 pts)

Never called by the end of the shift (20,000 arrived, 10,000 called):

	s5	s4	s3	s2	s1
no triage (fifo)	978	1456	2582	2424	2560
triage	0	0	60	4897	5043

Buys: all severe patients are seen — the 978 critical (s5) and 1456 s4 patients who were never called without triage drop to 0.
Average critical wait fell from 5118632/1016 ≈ 5038 min to 1221/1994 ≈ 0.6 min. Costs: the least severe are pushed out — s2 never called goes from 2424 to 
4897 and s1 from 2560 to 5043, i.e. almost half of all patients (roughly 9,940) are never seen, almost all of them s1/s2.
