package com.elenaneacsu.healthmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import com.elenaneacsu.healthmate.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_layout, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textview_measure);

        String currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem);
        }

        return convertView;
    }
}
