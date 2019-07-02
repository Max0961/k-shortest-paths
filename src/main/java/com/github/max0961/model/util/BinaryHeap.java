package com.github.max0961.model.util;

import java.util.*;

public class BinaryHeap<T extends Comparable<T>> {
    protected static final int DEFAULT_CAPACITY = 10;
    protected T[] items;
    protected int size;

    @SuppressWarnings("unchecked")
    public BinaryHeap() {
        items = (T[]) new Comparable[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public int size(){
        return size;
    }

    public BinaryHeap(Collection<T> collection) {
        this();
        for (T item : collection) {
            items[size++] = item;
            if (size >= items.length - 1) {
                items = resize();
            }
        }
        for (int i = (size - 1) / 2; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException("The heap is empty.");
        }
        return items[0];
    }

    @SuppressWarnings("unchecked")
    public void add(T value) {
        if (size == items.length - 1) {
            items = resize();
        }
        items[size++] = value;
        bubbleUp();
    }

    public T remove() {
        T result = peek();
        items[0] = items[size - 1];
        items[size] = null;
        size--;
        bubbleDown(0);
        return result;
    }

    private void bubbleDown(int index) {
        int smallerChild = index;

        if (hasLeftChild(index)
                && items[leftChildIndex(index)].compareTo(items[smallerChild]) < 0) {
            smallerChild = leftChildIndex(index);
        }

        if (hasRightChild(index)
                && items[rightChildIndex(index)].compareTo(items[smallerChild]) < 0) {
            smallerChild = rightChildIndex(index);
        }

        if (index != smallerChild) {
            swap(index, smallerChild);
            bubbleDown(smallerChild);
        }
    }

    private void bubbleUp() {
        int index = this.size - 1;

        while (index > 0 && parent(index).compareTo(items[index]) > 0) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    protected int leftChildIndex(int i) {
        return i * 2 + 1;
    }

    protected int rightChildIndex(int i) {
        return i * 2 + 2;
    }

    protected boolean hasLeftChild(int i) {
        return leftChildIndex(i) < size;
    }

    protected boolean hasRightChild(int i) {
        return rightChildIndex(i) < size;
    }

    protected T parent(int i) {
        return items[parentIndex(i)];
    }

    protected int parentIndex(int i) {
        return (i - 1) / 2;
    }

    private T[] resize() {
        return Arrays.copyOf(items, items.length * 2);
    }

    private T tmp;
    private void swap(int index1, int index2) {
        tmp = items[index1];
        items[index1] = items[index2];
        items[index2] = tmp;
    }
}
