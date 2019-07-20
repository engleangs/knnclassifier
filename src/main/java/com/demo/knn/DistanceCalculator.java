package com.demo.knn;
/**
 * @author Engleang
 * Distance calculator using Euclidean fomula
 *
 * */
public final class DistanceCalculator {

    public final static double getDistance(double[] data1, double[] data2) {
        if (data1.length != data2.length) {
            throw new IllegalArgumentException(" Data length mismatched ! ");
        }
        double sum = 0;
        for(int i =0;i<data1.length;i++) {
            sum += Math.pow( data1[i] - data2[i],2);
        }
        return Math.sqrt( sum);
    }

}
