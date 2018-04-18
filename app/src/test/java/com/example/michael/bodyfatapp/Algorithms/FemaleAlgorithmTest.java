package com.example.michael.bodyfatapp.Algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michael on 13/04/2018.
 */
public class FemaleAlgorithmTest {
    //generate measurement doubles
    double dHip = 70;
    double dTricep = 5;
    double dSubscap = 10;
    double dChin = 2;
    double dKnee = 19;
    double dWeight = 65;

    //create the female algorithm object
    FemaleAlgorithm object = new FemaleAlgorithm(dTricep, dChin, dSubscap, dHip, dKnee, dWeight);

    //create an offset
    double delta = 0.1;

    //test the getBodyFatMass Method
    @Test
    public void getBodyFatMass() throws Exception {
        //set the output and expected values
        double output = object.getBodyFatMass(object);
        double expected = 14.6 ;
        assertEquals(expected, output,delta);
    }
    @Test
    public void getBodyFatPercentage() throws Exception {
        double mass = object.getBodyFatMass(object);
        double expected = 22.4;
        double output =  object.getBodyFatPercentage();

        assertEquals(expected,output, delta);
    }

}