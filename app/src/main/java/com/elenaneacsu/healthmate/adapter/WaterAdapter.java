package com.elenaneacsu.healthmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.WaterRecord;

import java.util.List;

import static com.elenaneacsu.healthmate.utils.TimeUtils.stringifiedDate;

public class WaterAdapter extends RecyclerView.Adapter<WaterAdapter.WaterViewHolder> {
    private List<WaterRecord> mWaterRecordList;

    public WaterAdapter(List<WaterRecord> waterRecordList) {
        mWaterRecordList = waterRecordList;
    }

    @NonNull
    @Override
    public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.weight_item, viewGroup, false);
        return new WaterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterViewHolder waterViewHolder, int i) {
        WaterRecord waterRecord = mWaterRecordList.get(i);
        if(waterRecord!=null) {
            waterViewHolder.mTextViewDate.setText("Date: "+stringifiedDate(waterRecord.getDate()));
            waterViewHolder.mTextViewQuantity.setText("Quantity: "+String.valueOf(waterRecord.getQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return mWaterRecordList.size();
    }

    class WaterViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewDate;
        private TextView mTextViewQuantity;

        public WaterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewDate = itemView.findViewById(R.id.textview_date);
            mTextViewQuantity = itemView.findViewById(R.id.textview_weight);
        }
    }
}
