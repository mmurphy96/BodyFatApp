package com.example.michael.bodyfatapp.Algorithms;

/**
 * Created by Michael on 11/03/2018.
 */

//Create the algorithm for implementation of male body fat calculations
//BFMNew (kg) for men = −40.750 + [(0.397 × waist circumference) + [6.568 × (log triceps SF + log subscapular SF + log abdominal SF)]]
//Skinfold Measurements are recorded in millimetres and circumferences are recorded in centimetres
import java.lang.Math;


public class MaleAlgorithm {
    //crete the variables
    private double tricep, abdominal, subscapular, waistCircum , weight;
    private double bodyFatMass, bfPercent;

        //constructor to hold all values needed for the result
    public MaleAlgorithm(double tricep, double abs, double subscap, double waistC, double weight){
        this.tricep = tricep;
        this.abdominal = abs;
        this.subscapular = subscap;
        this.waistCircum = waistC;
        this.weight = weight;

    }

    public Double getBodyFatMass(MaleAlgorithm object){
        //algorithm will produce result equal to the mass of the body fat on the subject in kilograms
    bodyFatMass = -40.750 + ((0.397*waistCircum) + (6.568*(Math.log10(tricep) + Math.log10(subscapular) + Math.log10(abdominal))));

        if( bodyFatMass < 0 ){
            bodyFatMass = 0;
        }
    return bodyFatMass;
    }

    public Double getBodyFatPercentage(){
        bfPercent = (bodyFatMass/weight)*100;

        return bfPercent;
    }

}
