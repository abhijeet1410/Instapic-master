package com.rsmapps.selfieall.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.helper.Utils;

/**
 * Created by Mohammed.Irfan on 22-12-2017.
 */

public class AdapterSpinner extends ArrayAdapter<String> {

    private Activity context;
    private String[] fonts = {"blacklarch.ttf","blessd.ttf","fancy.ttf","fon.ttf","homestile.ttf",
                                "personaluse.ttf","smartwatch.ttf"};

    public AdapterSpinner(Activity context, int textViewResourceId,
                          String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        String data = getItem(position);
        LayoutInflater inflater= LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.custom_textview_to_spinner, parent, false);
        TextView label=(TextView)row.findViewById(R.id.txtSpnr);
        label.setText(data);
        Typeface  typeface = Utils.getCustomTypeface(context,"fonts/"+fonts[position]);
        label.setTypeface(typeface);
        return row;
    }

}