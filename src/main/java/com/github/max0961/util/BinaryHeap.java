package com.github.max0961.util;

import com.github.max0961.model.Vertex;

import java.util.*;

public class BinaryHeap<T extends Comparable<T>> extends PriorityQueue<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private T[] items;
    private int size;

    @SuppressWarnings("unchecked")
    public BinaryHeap() {
        items = (T[]) new Comparable[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
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

    public boolean add(T value) {
        if (size >= items.length - 1) {
            items = resize();
        }
        items[size++] = value;
        bubbleUp();
        return true;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException();
        }
        return items[0];
    }

    public T remove() {
        T result = peek();
        items[0] = items[size - 1];
        items[size] = null;
        size--;
        bubbleDown(0);
        return result;
    }

    public String toString() {
        return Arrays.toString(items);
    }

    private void bubbleDown(int index) {
        int smallerChild = index;

        if (hasLeftChild(index)
                && items[leftChild(index)].compareTo(items[smallerChild]) < 0) {
            smallerChild = leftChild(index);
        }

        if (hasRightChild(index)
                && items[rightChild(index)].compareTo(items[smallerChild]) < 0) {
            smallerChild = rightChild(index);
        }

        if (index != smallerChild) {
            swap(index, smallerChild);
            bubbleDown(smallerChild);
        }
    }

    private void bubbleUp() {
        int index = this.size - 1;

        while (parent(index).compareTo(items[index]) > 0
                && items[parentIndex(index)].compareTo(items[index]) > 0) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    private void bubbleUp(int index, T item) {
        if (items[index].compareTo(item) < 0) {
            throw new IllegalArgumentException("Новый ключ больше предыдущего");
        }

        while (index >= 0 && items[parentIndex(index)].compareTo(items[index]) > 0) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    private int leftChild(int i) {
        return i * 2 + 1;
    }

    private int rightChild(int i) {
        return i * 2 + 2;
    }

    private boolean hasLeftChild(int i) {
        return leftChild(i) < size;
    }

    private boolean hasRightChild(int i) {
        return rightChild(i) < size;
    }

    private T parent(int i) {
        return items[parentIndex(i)];
    }

    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    private T[] resize() {
        return Arrays.copyOf(items, items.length * 2);
    }

    private void swap(int index1, int index2) {
        T tmp = items[index1];
        items[index1] = items[index2];
        items[index2] = tmp;
    }
}
