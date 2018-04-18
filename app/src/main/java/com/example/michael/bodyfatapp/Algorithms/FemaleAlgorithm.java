package com.example.michael.bodyfatapp.Algorithms;


/**
 * Created by Michael on 11/03/2018.
 */

//BFMNew (kg) for women = −75.231 + [(0.512 × hip circumference) + [8.889 × (log chin SF + log triceps SF + log subscapular SF)] + (1.905 × knee breadth)].

public class FemaleAlgorithm {
    //create the variables needed
    private double tricep, chin, subscapular, hipCircumfernece, kneeBreadth, weight;
    private double bodyFatMass, bfPercent;

    //constructor
    public FemaleAlgorithm(double tricep, double chin, double subscap, double hipCircum, double kneebreadth, double weight) {
        this.tricep = tricep;
        this.chin = chin;
        this.subscapular = subscap;
        this.hipCircumfernece = hipCircum;
        this.kneeBreadth = kneebreadth;
        this.weight = weight;
    }
    //calculate the body fat mass of the user
    public double getBodyFatMass(FemaleAlgorithm object) {
        //algorithm will produce bodyFatMass equal to the mass of the body fat on the subject in kilograms
        bodyFatMass = -75.231 + ((0.512 * hipCircumfernece) + (8.889 * (Math.log10(chin) + Math.log10(tricep) + Math.log10(subscapular)) + (1.905 * kneeBreadth)));

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
