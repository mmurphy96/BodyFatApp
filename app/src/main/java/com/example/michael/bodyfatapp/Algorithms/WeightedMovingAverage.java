package com.example.michael.bodyfatapp.Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Michael on 21/03/2018.
 */

public class WeightedMovingAverage {
    //hashmap to hold the body fat values and a second to hold the weighted values
    private static HashMap<Integer,Double> originalValues = new HashMap<>();
    private static HashMap<Integer,Double> weightedValues = new HashMap<>();
    private static double madValue;

/* window size used to determine how many periods will be
 * used to calculate the future value
 */
    private  static int size = 3;

    public WeightedMovingAverage(HashMap<Integer,Double> originalValues){
        //create the weights used for the forecasting
        double weight[]  = {0.2,0.3,0.5};
        int j =0;
        double weightedVal;

        for (int i = 0; i + size <= originalValues.size() ; i++) {
            //values must be reset to 0 after each iteration
            double sum = 0;
            int k =0;
            //iterate through the body fat results and apply the weight.
            for (j = i; j < i + size; j++) {
                weightedVal = originalValues.get(j)*weight[k];
              //  System.out.println("Weighted Val:"+  weightedVal);
                sum += weightedVal;
                k++;
                if (k==3)
                    //reset the weight back to the initial value before the next iteration
                    k=1;
            }
            //add the weighted values to the hash map
            weightedValues.put(j-size,sum);
        }
        //calculate the mean absolute deviation
        calMAD(weightedValues, originalValues);
    }

    //Method is used to calculate the Mean Absolute Distribution
    private static void calMAD( Map values, HashMap<Integer,Double> ogValues) {
        //array list to hold calculated values
        List<Double> MAD = new ArrayList<Double>();
        int counter = size ;
        boolean last = false;

        Iterator it = values.entrySet().iterator();

        //iterate through the predicted value hash map
        while (it.hasNext()){

            Map.Entry pair = (Map.Entry)it.next();
            int key = (int) pair.getKey();
            double val = (double) pair.getValue();
            if (key<ogValues.size()-3) {
                double ogValue = ogValues.get(counter);
                //subtract the original value minus the predicted
                double MADval = ogValue - val;
                //add the value to the list
                MAD.add(MADval);
                counter ++;
            }
        }
        double sum = 0;
        for(Double d : MAD)
            sum += d;
        //madValue is the final averaged value
            madValue = sum/MAD.size();
        //System.out.println(madValue);

    }

    public static HashMap<Integer, Double> getWeightedValues() {
        return weightedValues;
    }

    public static double getForecastValue() {
        double fval = weightedValues.get(weightedValues.size()-1);
        //zero refers to the last added key
     return fval;
    }

    public static int getSize() {
        return size;
    }

    public static double getMadValue() {
        return madValue;
    }
}
