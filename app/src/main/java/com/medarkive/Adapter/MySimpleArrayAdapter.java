package com.medarkive.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Beans.TrackerDataBean;

public class MySimpleArrayAdapter extends ArrayAdapter<TrackerDataBean> {
	 
    private final Context context;
    private final ArrayList<TrackerDataBean> itemsArrayList;

    public MySimpleArrayAdapter(Context context, ArrayList<TrackerDataBean> itemsArrayList) {

        super(context, R.layout.activity_tracker, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.tracker_item_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getTitle());
        valueView.setText(itemsArrayList.get(position).getSpentTime());

        // 5. retrn rowView
        return rowView;
    }
}