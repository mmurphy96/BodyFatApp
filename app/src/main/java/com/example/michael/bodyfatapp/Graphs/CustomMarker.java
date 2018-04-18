package com.example.michael.bodyfatapp.Graphs;

import android.content.Context;
import android.widget.TextView;

import com.example.michael.bodyfatapp.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

/**
 * Created by Michael on 27/03/2018.
 */

public class CustomMarker extends MarkerView {
    private TextView mGraphMarker;
    private String date;
    DecimalFormat decimalFormat = new DecimalFormat();
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarker(Context context, int layoutResource,String date) {
        super(context, layoutResource);
        this.date = date;
        mGraphMarker = findViewById(R.id.graphMarkerText);
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        int x = (int) Math.round(e.getX());

        if (x<GraphMain.formatXAxis.output.size()) {

            decimalFormat.setMaximumFractionDigits(3);
            mGraphMarker.setText(GraphMain.formatXAxis.output.get(x) + " " + decimalFormat.format(e.getY()) + "%");
            super.refreshContent(e, highlight);
        }
        else{
            mGraphMarker.setText("Predicted Value: " + decimalFormat.format(e.getY()) + "%");
            super.refreshContent(e, highlight);
        }
    }

    private MPPointF mOffset;
    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }
        return mOffset;
    }
}
