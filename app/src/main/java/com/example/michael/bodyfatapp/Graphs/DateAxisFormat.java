package com.example.michael.bodyfatapp.Graphs;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateAxisFormat {

    ArrayList<Long> dates = new ArrayList<>();
    ArrayList<String> output = new ArrayList<>();

    String outputDate;

    public DateAxisFormat(ArrayList<Long> dates){
        this.dates = dates;
        //create the simple date format object -- 10-JAN
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");

        for (int i =0; i < dates. size(); i++)
        {
            //iterate through the dates and format each one
            Date date = new Date(dates.get(i));
            outputDate = dateFormat.format(date);

            output.add(outputDate);
        }
    }



}

