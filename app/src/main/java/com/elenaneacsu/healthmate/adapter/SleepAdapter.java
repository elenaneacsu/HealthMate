package com.elenaneacsu.healthmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elenaneacsu.healthmate.model.SleepRecord;
import com.elenaneacsu.healthmate.R;

import java.util.List;

import static com.elenaneacsu.healthmate.utils.TimeUtils.stringifiedDate;

public class SleepAdapter extends RecyclerView.Adapter<SleepAdapter.SleepViewHolder> {
    private List<SleepRecord> mSleepRecordList;

    public SleepAdapter(List<SleepRecord> sleepRecordList) {
        mSleepRecordList = sleepRecordList;
    }

    @NonNull
    @Override
    public SleepAdapter.SleepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.weight_item, viewGroup, false);
        return new SleepAdapter.SleepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SleepAdapter.SleepViewHolder sleepViewHolder, int i) {
        SleepRecord sleepRecord = mSleepRecordList.get(i);
        if(sleepRecord!=null) {
            sleepViewHolder.mTextViewDate.setText("Date: "+stringifiedDate(sleepRecord.getDate()));
            sleepViewHolder.mTextViewQuantity.setText("Hours of sleep: "+String.valueOf(sleepRecord.getHours()));
        }
    }

    @Override
    public int getItemCount() {
        return mSleepRecordList.size();
    }

    class SleepViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewDate;
        private TextView mTextViewQuantity;

        public SleepViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewDate = itemView.findViewById(R.id.textview_date);
            mTextViewQuantity = itemView.findViewById(R.id.textview_weight);
        }
    }
}
