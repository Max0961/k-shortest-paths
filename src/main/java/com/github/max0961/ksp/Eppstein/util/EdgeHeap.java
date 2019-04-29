package com.github.max0961.ksp.Eppstein.util;

import com.github.max0961.model.DirectedEdge;
import com.github.max0961.util.BinaryHeap;

public final class EdgeHeap extends BinaryHeap<DirectedEdge> {
    public EdgeHeap() {
        items = new DirectedEdge[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public DirectedEdge[] edges() {
        return items;
    }

    public DirectedEdge getEdge(int index) {
        return items[index];
    }

    public DirectedEdge parent(int i) {
        if (i == 0) return null;
        return items[parentIndex(i)];
    }

    public int leftChildIndex(int i) {
        return i * 2 + 1;
    }

    public int rightChildIndex(int i) {
        return i * 2 + 2;
    }

    public boolean hasLeftChild(int i) {
        return leftChildIndex(i) < size;
    }

    public boolean hasRightChild(int i) {
        return rightChildIndex(i) < size;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        int j = 0;
        int levelLength = 1;
        while (i < size) {
            while (j < levelLength) {
                DirectedEdge e = items[i + j];
                if (e == null) break;
                stringBuilder.append(e.getIncrementalWeight()).append("\t");
                ++j;
            }
            i += levelLength;
            levelLength *= 2;
            j = 0;
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
