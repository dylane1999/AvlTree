package com.hwcompsci2;
public class SortedArrayMap implements Map {

    class Definition {
        String key;
        String value;

        public Definition(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private int size;
    private Definition[] array;

    public SortedArrayMap() {
        this.size = 0;
        this.array = new Definition[10];
    }

    public int size() {
        return this.size;
    }

    public boolean put(String key, String value) {
        int index = this.binarySearch(key, true);
        this.add(index, new Definition(key, value));
        return (index < this.size && this.array[index].key.equals(key));
    }

    private void add(int index, Definition new_element) {
        if (this.size == this.array.length) {
            Definition[] new_array = new Definition[2 * this.array.length];
            for (int i = 0; i < this.array.length; i++) {
                new_array[i] = this.array[i];
            }
            this.array = new_array;
        }
        this.array[this.size] = new_element;
        for (int i = this.size; i > index; i--) {
            this.array[i] = this.array[i - 1];
        }
        this.array[index] = new_element;
        this.size++;
    }

    private int binarySearch(String key, boolean insertion) {
        int lo = 0;
        int hi = this.size - 1;
        int mid;
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            int compare = key.compareTo(this.array[mid].key);
            if (compare < 0) {
                hi = mid - 1;
            } else if (compare == 0) {
                return mid;
            } else if (compare > 0) {
                lo = mid + 1;
            }
        }
        if (insertion) {
            return lo;
        } else {
            return -1;
        }
    }

    public void clear() {
        this.size = 0;
    }

    public void print() {
        for (int i = 0; i < this.size; i++) {
            System.out.println(this.array[i].key);
        }
        System.out.println();
    }

    public String get(String key) {
        int index = this.binarySearch(key, false);
        if (index == -1) {
            return null;
        } else {
            return this.array[index].value;
        }
    }

    public static long ascendingTest(Map map, long numElements, int numTrials) {
        String formatString = "%0" + String.format("%d", ((long) Math.log10(numElements)) + 2) + "d";
        long start;
        long end;
        long[] data = new long[numTrials];
        int trial;
        long i;
        for (trial = 0; trial < numTrials; trial++) {
            start = System.nanoTime();
            // add numbers from small to large
            for (i = 0; i < numElements; i++) {
                map.put(String.format(formatString, i), "");
            }
            end = System.nanoTime();
            data[trial] = end - start;
        }
        long total = 0;
        for (trial = 0; trial < numTrials; trial++) {
            total += data[trial];
        }
        return (total / numTrials) / 1000000;
    }

    public static long descendingTest(Map map, long numElements, int numTrials) {
        String formatString = "%0" + String.format("%d", ((long) Math.log10(numElements)) + 2) + "d";
        long start;
        long end;
        long[] data = new long[numTrials];
        int trial;
        long i;
        for (trial = 0; trial < numTrials; trial++) {
            start = System.nanoTime();
            // add numbers from large to small
            for (i = numElements; i > 0; i--) {
                map.put(String.format(formatString, i), "");
            }
            end = System.nanoTime();
            data[trial] = end - start;
        }
        long total = 0;
        for (trial = 0; trial < numTrials; trial++) {
            total += data[trial];
        }
        return (total / numTrials) / 1000000;
    }

    public static void main(String[] args) {
        long result;
        long numElements = 10000;
        int numTrials = 100;
        Map map;

        map = new AVLTreeMap();
        result = ascendingTest(map, numElements, numTrials);
        System.out.println(String.format("forward  AVL\t%d milliseconds", result));

        map = new SortedArrayMap();
        result = ascendingTest(map, numElements, numTrials);
        System.out.println(String.format("forward  array\t%d milliseconds", result));

        map = new AVLTreeMap();
        result = descendingTest(map, numElements, numTrials);
        System.out.println(String.format("backward AVL\t%d milliseconds", result));

        map = new SortedArrayMap();
        result = descendingTest(map, numElements, numTrials);
        System.out.println(String.format("backward array\t%d milliseconds", result));
    }
}