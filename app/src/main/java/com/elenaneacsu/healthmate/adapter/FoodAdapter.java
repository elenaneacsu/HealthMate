package com.elenaneacsu.healthmate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.Hint;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    private List<Hint> mHintList;
    private ItemClickListener mItemClickListener;
    private Context mContext;

    public FoodAdapter(List<Hint> hintList, Context context) {
        this.mHintList = hintList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_item, viewGroup, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
        Hint hint = mHintList.get(i);
        if(hint!=null) {
            foodViewHolder.mTextViewName.setText(hint.getFood().getLabel());
            foodViewHolder.mTextViewBrand.setText(hint.getFood().getBrand());
            foodViewHolder.mTextViewNutrients.setText(hint.getFood().getNutrients().toString());
        }
    }

    @Override
    public int getItemCount() {
        if (mHintList == null) {
            return 0;
        } else {
            return mHintList.size();
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, Hint hint);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewName;
        private TextView mTextViewBrand;
        private TextView mTextViewNutrients;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewName = itemView.findViewById(R.id.textview_foodname);
            mTextViewBrand = itemView.findViewById(R.id.textview_foodbrand);
            mTextViewNutrients = itemView.findViewById(R.id.textview_fooddescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mHintList.get(getAdapterPosition()));
            }
        }
    }
}
