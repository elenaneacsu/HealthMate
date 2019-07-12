package com.elenaneacsu.healthmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.WeightRecord;

import java.util.List;

import static com.elenaneacsu.healthmate.utils.TimeUtils.stringifiedDate;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {
    private List<WeightRecord> mWeightRecordList;

    public WeightAdapter(List<WeightRecord> weightRecordList) {
        mWeightRecordList = weightRecordList;
    }

    @NonNull
    @Override
    public WeightViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.weight_item, viewGroup, false);
        return new WeightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder weightViewHolder, int i) {
        WeightRecord weightRecord = mWeightRecordList.get(i);
        if (weightRecord != null) {
            weightViewHolder.mTextViewDate.setText("Date: " + stringifiedDate(weightRecord.getDate()));
            weightViewHolder.mTextViewWeight.setText("Weight: " + String.valueOf(weightRecord.getWeight()));
        }
    }

    @Override
    public int getItemCount() {
        return mWeightRecordList.size();
    }

    public class WeightViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewDate;
        private TextView mTextViewWeight;

        public WeightViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewDate = itemView.findViewById(R.id.textview_date);
            mTextViewWeight = itemView.findViewById(R.id.textview_weight);
        }
    }
}
