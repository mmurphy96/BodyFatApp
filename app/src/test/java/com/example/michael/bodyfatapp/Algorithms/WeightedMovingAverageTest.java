package com.example.michael.bodyfatapp.Algorithms;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Michael on 13/04/2018.
 */
public class WeightedMovingAverageTest {
    double delta =0.01;
    HashMap<Integer,Double> map = new HashMap<>();
    HashMap<Integer,Double> expected = new HashMap<>();
    HashMap<Integer,Double> output = new HashMap<>();
    @Test
    public void getWeightedValues() throws Exception {
        //add values to the hashmap
        map.put(0,10.0);
        map.put(1,10.15);
        map.put(2,10.20);
        map.put(3,10.25);
        map.put(4,10.45);
        //create the weighted moving average obj
        WeightedMovingAverage wma = new WeightedMovingAverage(map);
        //set the output
        output = WeightedMovingAverage.getWeightedValues();
        //set the expected values
        expected.put(0,10.145);
        expected.put(1,10.215);
        expected.put(2,10.34);
        assertEquals(expected, output);
    }
    @Test
    public void getMadValue() throws Exception {
        map.put(0,10.0);
        map.put(1,10.15);
        map.put(2,10.35);
        map.put(3,10.25);
        map.put(4,10.45);
        WeightedMovingAverage wma = new WeightedMovingAverage(map);
        double out1;
        double expected1 = 0.11;
        out1 = wma.getMadValue();
        assertEquals(expected1, out1, delta);
    }

}