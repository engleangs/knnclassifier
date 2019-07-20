package com.demo.knn;
/**
 * @author
 * Data class for storing the number represent bit(0,1) of the handwriting data.
 *
 * */
public class SampleData {
    private final double []data;
    private final String identifier;
    private final String fileName;
    public SampleData(String identifier, double[] data, String fileName) {
        this.data = data;
        this.identifier = identifier;
        this.fileName = fileName;
    }

    public double[] getData() {
        return data;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFileName() {
        return fileName;
    }
}
