package com.example.michael.bodyfatapp.Algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michael on 13/04/2018.
 */
public class MaleAlgorithmTest {

    double tricep = 6;
    double abdominal = 16;
    double subscapular = 7;
    double waistCircum =74;
    double weight = 75;
    MaleAlgorithm maleAlgorithm = new MaleAlgorithm(tricep,abdominal,subscapular,waistCircum,weight);
    double delta = 0.1;
    @Test
    public void getBodyFatMass() throws Exception {
        double output = maleAlgorithm.getBodyFatMass(maleAlgorithm);
        double expected = 7.2 ;
        assertEquals(expected, output,delta);

    }

    @Test
    public void getBodyFatPercentage() throws Exception {
        double mass = maleAlgorithm.getBodyFatMass(maleAlgorithm);
        double expected = 9.6;
        double output =  maleAlgorithm.getBodyFatPercentage();

        assertEquals(expected, output,delta);

    }

}