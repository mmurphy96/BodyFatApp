package com.example.michael.bodyfatapp.Graphs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.michael.bodyfatapp.Algorithms.WeightedMovingAverage;
import com.example.michael.bodyfatapp.Login_Register.MainActivity;
import com.example.michael.bodyfatapp.R;
import com.example.michael.bodyfatapp.Recordings.FemaleMeasurement;
import com.example.michael.bodyfatapp.Recordings.MaleMeasurement;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GraphMain extends AppCompatActivity {

    private LineChart mChart;
    private Button mGenerateGraph;
    private Spinner mProfileSel, mSite;
    private TextView mChartTitle;
    private String[] maleSites = new String[]{"Body Fat Percentage","Body Fat Mass", "Tricep", "Abdominal", "Subscapular", "Waist Circumference","Weight"};
    private String[] femaleSites = new String[]{"Body Fat Percentage","Body Fat Mass", "Tricep", "Chin", "Subscapular", "Hip Circumference", "Knee Breadth","Weight"};
    private String profileSelection, gender,hdate;
    public ArrayList<Object> list = new ArrayList<>();
    public ArrayList<Long> dates;
    private ArrayList<Entry> graphArray, forecastArray;
    private ArrayList<ILineDataSet> dataSets;
    private HashMap<Integer,Double> wmaMap ;
    private LineDataSet set1, set2;
    public static DateAxisFormat formatXAxis;



    private List<MaleMeasurement> maleMeasurementList;
    private List<FemaleMeasurement> femaleMeasurementList ;

    private String Subscapular, Abdominal, Tricep, WaistCircumference,Weight, BodyFatMass, BodyFatPercentage;
    private Long Date;
    private String Chin, HipCircumference, KneeBreadth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_main);
        setStatusBarTranslucent(true);
        //EnableAction bar for back Button
    //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChart = findViewById(R.id.LineChart);
 //      mChart.setOnChartGestureListener(GraphMain.this);
//        mChart.setOnChartValueSelectedListener(GraphMain.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);


        //initiate the buttons etc
        mGenerateGraph = findViewById(R.id.GenerateGraph);
        mSite = findViewById(R.id.gSelSite);
        mProfileSel = findViewById(R.id.gSelProfile);
        mChartTitle = findViewById(R.id.chartTitle);

        //create the adapter for the profile selection
        final ArrayAdapter<String> profileAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, MainActivity.profileNameList);
        //set the adapter view to the profile spinner
        mProfileSel.setAdapter(profileAdapter);

        //set the sites based on the profile selection
        mProfileSel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //gather a list of the genders based on the position of the profile name in the spinner.
                gender = MainActivity.profileGenderList.get(position);

                ((TextView) parent.getChildAt(0)).setTextSize(20);
                // if the gender is male, set the adapter to populate maleSites list
                if (gender.equals("Male")) {
                    ArrayAdapter<String> maleSiteAdapter =
                            new ArrayAdapter<>(GraphMain.this, android.R.layout.simple_list_item_1, maleSites);
                    mSite.setAdapter(maleSiteAdapter);
                } else if (gender.equals("Female")) {
                    // if the gender is female, set the adapter to populate femaleSites list
                    ArrayAdapter<String> femaleSiteAdapter =
                            new ArrayAdapter<>(GraphMain.this, android.R.layout.simple_list_item_1, femaleSites);
                    mSite.setAdapter(femaleSiteAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(20);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mGenerateGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleMeasurementList = new ArrayList<MaleMeasurement>();
                femaleMeasurementList = new ArrayList<FemaleMeasurement>();
                //get the name of the selected profile
                profileSelection = mProfileSel.getSelectedItem().toString();

                list.clear();

                //add the selected profile measurements hash map to an array list
                list.add(MainActivity.hashmap.get(profileSelection));

                //for each of the array list measurements iterate through
                for (int i = 0; i < list.size(); i++) {
                    //create an array list to hold each hash map
                    ArrayList<Map> obj = (ArrayList<Map>) list.get(i);
                    //iterate through each hash map to get skinfold measurements
                    for (int j = 0; j < obj.size(); j++) {
                        Map<String, String> hash = obj.get(j);

                        if (gender.equals("Male")) {
                            Abdominal = hash.get("Abdominal");
                            Tricep = hash.get("Tricep");
                            Subscapular = hash.get("Subscapular");
                            WaistCircumference = hash.get("WaistCircumference");
                            Weight = hash.get("Weight");
                            BodyFatMass = hash.get("BodyFatMass");
                            BodyFatPercentage = hash.get("BodyFatPercentage");
                            hdate = String.valueOf(hash.get("Date"));

                            Date = Long.valueOf(hdate);
                            maleMeasurementList.add(
                                    new MaleMeasurement(Tricep, Abdominal, Subscapular, Weight, WaistCircumference, BodyFatMass,BodyFatPercentage, Date));
                        }
                        if (gender.equals("Female")) {
                            Chin = hash.get("Chin");
                            Tricep = hash.get("Tricep");
                            Subscapular = hash.get("Subscapular");
                            HipCircumference = hash.get("HipCircumference");
                            KneeBreadth = hash.get("KneeBreadth");
                            BodyFatMass = hash.get("BodyFatMass");
                            BodyFatPercentage = hash.get("BodyFatPercentage");
                            hdate = String.valueOf(hash.get("Date"));
                            Date = Long.valueOf(hdate);

                            //Create a list of female measurement objects
                            //this can be use to populate the graphs
                            femaleMeasurementList.add(new FemaleMeasurement(Tricep, Chin, Subscapular, HipCircumference, KneeBreadth,Weight, BodyFatMass,BodyFatPercentage, Date));


                        }

                    }

                }
                buildGraph();
            }


        });

    }

    //Return to Main Menu
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void buildGraph() {

        dates = new ArrayList<>();
        graphArray = new ArrayList<>();
        String yValue = "";

        //get the selected site
        String selectedSite = mSite.getSelectedItem().toString();

        //list for wma
        List<String> wmaList = new ArrayList<String>();

        dataSets = new ArrayList<>();

        Collections.sort(maleMeasurementList, new Comparator<MaleMeasurement>() {
            @Override
            public int compare(MaleMeasurement o1, MaleMeasurement o2) {
                //sort the list of objects via the long - date
                return Long.valueOf(o1.getDate()).compareTo(Long.valueOf(o2.getDate()));
            }
        });

        Collections.sort(femaleMeasurementList, new Comparator<FemaleMeasurement>() {
            @Override
            public int compare(FemaleMeasurement o1, FemaleMeasurement o2) {
                return Long.valueOf(o1.getDate()).compareTo(Long.valueOf(o2.getDate()));
            }
        });

        if (gender.equals("Male")) {
            //populate the array for the graph with x and y values
            for (int i = 0; i < maleMeasurementList.size(); i++) {

                if (selectedSite.equals("Tricep")){
                    yValue = maleMeasurementList.get(i).getTricep();
                }
                if (selectedSite.equals("Abdominal")){
                    yValue = maleMeasurementList.get(i).getAbdominal();
                }
                if (selectedSite.equals("Subscapular")){
                    yValue = maleMeasurementList.get(i).getSubscapular();
                }
                if (selectedSite.equals("Waist Circumference")){
                    yValue = maleMeasurementList.get(i).getWaistCircumference();
                }
                if (selectedSite.equals("Weight")){
                    yValue = maleMeasurementList.get(i).getWeight();
                }
                if (selectedSite.equals("Body Fat Percentage")){
                    yValue = maleMeasurementList.get(i).getBodyFatPercentage();
                    wmaList.add(yValue);
                }
                if (selectedSite.equals("Body Fat Mass")) {
                    yValue = maleMeasurementList.get(i).getBodyFatMass();
                    wmaList.add(yValue);
                }
                //parse the y value to a float for the graph
                float fYValue = Float.parseFloat(yValue);
                //get the long value of the date generated by firebase
                Long date = maleMeasurementList.get(i).getDate();
                //Convert it to a readable format
                dates.add(date);
                //add values to the date
                graphArray.add(new Entry(i,fYValue));

            }

        }
        if (gender.equals("Female")) {
            //Set the y values of the graph
            for (int i = 0; i < femaleMeasurementList.size(); i++) {

                if (selectedSite.equals("Tricep")){

                    yValue = femaleMeasurementList.get(i).getTricep();
                }
                if (selectedSite.equals("Chin")){
                    yValue = femaleMeasurementList.get(i).getChin();
                }
                if (selectedSite.equals("Subscapular")){
                    yValue = femaleMeasurementList.get(i).getSubscapular();
                }
                if (selectedSite.equals("Hip Circumference")){
                    yValue = femaleMeasurementList.get(i).getHipCircumference();
                }
                if (selectedSite.equals("Knee Breadth")){
                    yValue = femaleMeasurementList.get(i).getKneeBreadth();
                }
                if (selectedSite.equals("Weight")){
                    yValue = femaleMeasurementList.get(i).getWeight();
                }
                if (selectedSite.equals("Body Fat Percentage")){
                    yValue = femaleMeasurementList.get(i).getBodyFatPercentage();
                    wmaList.add(yValue);
                }
                if (selectedSite.equals("Body Fat Mass")) {
                    yValue = femaleMeasurementList.get(i).getBodyFatMass();
                    wmaList.add(yValue);
                }

                //parse the y value to a float for the graph
                float fYValue = Float.parseFloat(yValue);

                //get the long value of the date generated by firebase
                Long date = femaleMeasurementList.get(i).getDate();

                //Convert it to a readable format
                dates.add(date);

                //add values to the date

                graphArray.add(new Entry(i,fYValue));
            }


        }
        set1 = new LineDataSet(graphArray, selectedSite);
        set1.setFillAlpha(110);
        //set the line colour
        set1.setColor(Color.BLACK);
        //set the line width
        set1.setLineWidth(3f);
        //draw circles on the data points and set colours
        set1.setDrawCircles(true);
        set1.setCircleColor(Color.BLUE);
        set1.setCircleRadius(6f);
        //set the displayed text size
        set1.setValueTextSize(12f);

        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(false);



        //create the x-axis
        XAxis xAxis = mChart.getXAxis();
        //set values to only show on the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //create the DateAxisFormat object need to display the dates correctly
         formatXAxis = new DateAxisFormat(dates);
        //set the dates to the x-axis
        xAxis.setValueFormatter(new IndexAxisValueFormatter(formatXAxis.output));


        CustomMarker customMarker = new CustomMarker(getApplicationContext(),R.layout.graph_marker,formatXAxis.outputDate);

        dataSets.add(set1);

        if ((selectedSite.equals("Body Fat Mass") | selectedSite.equals("Body Fat Percentage"))& wmaList.size()>3 ){


            //add each of the body fat results to the WMAmap for forecasting
            wmaMap = new HashMap<Integer, Double>();

            for(int i=0; i<wmaList.size();i++){
                double x = Double.parseDouble(wmaList.get(i));
                wmaMap.put(i,x);
            }
            //apply the forecasting
            WeightedMovingAverage forecast = new WeightedMovingAverage(wmaMap);

            //get the new x-value entry
            int x = graphArray.size() + 1;


            //getWeighted Value return the forecast value of the weighted averages
            double y = forecast.getForecastValue();
            float f = (float)y;

            double MAD = forecast.getMadValue();
            forecastArray = new ArrayList<>();
            //add the forecasted value to the graph
            forecastArray.add(new Entry(x,f));

            set2 = new LineDataSet(forecastArray, "Predicted Future Value");

            //customise the 2nd graph data i.e the wma value
            set2.setFillAlpha(110);
            set2.setColor(Color.RED);
            set2.setLineWidth(3f);
            set2.setDrawCircles(true);
            set2.setCircleColor(Color.RED);
            set2.setCircleRadius(6f);
            set2.setValueTextSize(12f);

            dataSets.add(set2);
            mChart.setMarkerView(customMarker);
            mChart.getDescription().setText("X axis: Dates, Y axis: " +selectedSite.toString());
            mChartTitle.setText("Time(Date) vs " + selectedSite.toString());


        }
        if ( selectedSite != "Body Fat Mass" ||selectedSite != "Body Fat Percentage"){
            //set the x-axis focus (center the graph)
            xAxis.setLabelCount(graphArray.size(), true);
            //set the chart descripton (bottom corner)
            mChart.getDescription().setText("X axis: Dates , Y axis: " + selectedSite);
            //Set the Text Vies item as the time vs the selected date
            mChartTitle.setText("Time(Date) vs " + selectedSite);
        }

        //animate the chart
        mChart.animateX(2000);
        LineData data = new LineData(dataSets);
    //set the chart data
        mChart.setData(data);
        //refresh the chart data
        mChart.invalidate();

    }


}
