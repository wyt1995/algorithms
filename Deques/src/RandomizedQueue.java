import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INIT_SIZE = 8;
    private Item[] items;
    private int size;
    private int arraySize;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.items = (Item[]) new Object[INIT_SIZE];
        this.size = 0;
        this.arraySize = INIT_SIZE;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, temp, 0, size);
        items = temp;
        arraySize = capacity;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == arraySize) {
            resize(arraySize * 2);
        }
        items[size] = item;
        size += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniformInt(size);
        int last = size - 1;
        Item item = items[index];
        items[index] = items[last];
        items[last] = null;
        size -= 1;
        if (arraySize > INIT_SIZE && size < 0.25 * arraySize) {
            resize(arraySize / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniformInt(size);
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomDequeIterator();
    }

    private class RandomDequeIterator implements Iterator<Item> {
        private Item[] randomized;
        private int position;

        public RandomDequeIterator() {
            this.randomized = (Item[]) new Object[size];
            System.arraycopy(items, 0, randomized, 0, size);
            StdRandom.shuffle(randomized);
            this.position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return randomized[position++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> deque = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            deque.enqueue("A" + i);
            deque.enqueue("B" + i);
        }

        for (String s : deque) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println("Size: " + deque.size());

        // sample
        for (int i = 0; i < 20; i++) {
            System.out.print(deque.sample() + " ");
        }
        System.out.println();
        System.out.println("Size: " + deque.size());

        // dequeue
        for (int i = 0; i < 20; i++) {
            System.out.print(deque.dequeue() + " ");
        }
        System.out.println();
        System.out.println("Size: " + deque.size());
    }
}
