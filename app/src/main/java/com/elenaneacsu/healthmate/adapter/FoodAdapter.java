package com.elenaneacsu.healthmate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.entity.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    private List<Food> mFoodList;
    private ItemClickListener mItemClickListener;
    private Context mContext;

    public FoodAdapter(List<Food> foodItemList, Context context) {
        this.mFoodList = foodItemList;
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
        Food food = mFoodList.get(i);
        if(food!=null) {
            foodViewHolder.mTextViewName.setText(food.getFoodName());
            foodViewHolder.mTextViewBrand.setText(food.getFoodType());
            foodViewHolder.mTextViewDescription.setText(food.getFoodDescription());
        }
    }

    @Override
    public int getItemCount() {
        if (mFoodList == null) {
            return 0;
        } else {
            return mFoodList.size();
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, Food food);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewName;
        private TextView mTextViewBrand;
        private TextView mTextViewDescription;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewName = itemView.findViewById(R.id.textview_foodname);
            mTextViewBrand = itemView.findViewById(R.id.textview_foodbrand);
            mTextViewDescription = itemView.findViewById(R.id.textview_fooddescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mFoodList.get(getAdapterPosition()));
            }
        }
    }
}
