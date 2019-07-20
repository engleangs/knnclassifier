package com.demo.knn;

/**
 * @author Engleang
 * Data class for storing distance value between test and sample .
 * */
public class SampleDistanceValue  {
    private final String identifier;
    private final double value;

    public SampleDistanceValue(String identifier, double value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public double getValue() {
        return value;
    }

}
