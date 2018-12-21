package com.github.max0961;

public class Vertex implements Comparable<Vertex>, Cloneable{
    private int label;
    private Double distance;

    public Vertex(int label) {
        this.label = label;
        this.distance = Double.MAX_VALUE;
    }

    public Vertex(int label, double distance) {
        this.label = label;
        this.distance = distance;
    }

    public int getLabel(){
        return label;
    }

    public Double getDistance(){
        return distance;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    @Override
    public int compareTo(Vertex other) {
        return this.distance.compareTo(other.distance);
    }
}
