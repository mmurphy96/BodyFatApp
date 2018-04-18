package com.example.michael.bodyfatapp.Recordings;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.michael.bodyfatapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Michael on 28/03/2018.
 */

/*expandable list class which generates the list in Measurements.java
    the list comprimises of a child and a parent header each containing strings of data
    the parent header should hold the BFP result and the child header should hold the information
    regarding the measurement
    */
public class ExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listHeader;
    private HashMap<String, List<String>> listChild;

    public ExpandableList(Context context, List<String> listHeader, HashMap<String, List<String>> listChild){
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;

    }

    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return this.listHeader.get(groupPosition);
    }

    @Override
    //return the selected position of the child list, which should be the same as the header list
    public Object getChild(int groupPosition, int childPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    //use the header string to get the position of the item
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String header = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //set the group view contents
            convertView = inflater.inflate(R.layout.list_header,null);
        }
        TextView listHeader =  convertView.findViewById(R.id.ListHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(header);

        return convertView;

    }

    @Override
    //use the header string to get the position of the item
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        //get the item in the list
        TextView childItem = (TextView) convertView
                .findViewById(R.id.List_Item1);
        //set the set to the retreived item
        childItem.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

