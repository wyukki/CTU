package cz.cvut.fel.pjv;

/**
 * Implementation of the {@link Queue} backed by fixed size array.
 */
public class CircularArrayQueue implements Queue {

    private String[] queue;
    private int capacity = 0;
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    /**
     * Creates the queue with capacity set to the value of 5.
     */
    public CircularArrayQueue() {
        this.capacity = 5;
        this.queue = new String[5];
    }

    /**
     * Creates the queue with given {@code capacity}. The capacity represents
     * maximal number of elements that the queue is able to store.
     *
     * @param capacity of the queue
     */
    public CircularArrayQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new String[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean isFull() {
        return size == this.capacity;
    }

    @Override
    public boolean enqueue(String obj) {
        if (this.queue != null  && !isFull()) {
            queue[tail % this.capacity] = obj;
            tail++;
            size++;
            return true;
        }
        return false;
    }

    @Override
    public String dequeue() {
        if (isEmpty()) {
            return null;
        }
        String element = this.queue[head % this.capacity];
        this.queue[head % this.capacity] = null;
        head++;
        size--;
        return element;
    }

    @Override
    public void printAllElements() {
        for (String s : this.queue) {
            if (s != null) System.out.println(s);
        }
    }
}
