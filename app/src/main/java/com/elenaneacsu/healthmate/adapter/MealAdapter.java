package com.elenaneacsu.healthmate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.FirestoreFood;
import com.elenaneacsu.healthmate.model.Meal;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MealAdapter extends ExpandableRecyclerViewAdapter<MealAdapter.MealViewHolder, MealAdapter.FoodMainViewHolder> {

    public MealAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public MealViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_item, parent, false);

        return new MealViewHolder(view);
    }

    @Override
    public FoodMainViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_main_item, parent, false);
        return new FoodMainViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(FoodMainViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final FirestoreFood food = (FirestoreFood) group.getItems().get(childIndex);
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mFoodClickListener.onOptionsMenuClick(v, food.ge());
//            }
//        };
        //holder.getImageButtonOptions().setOnClickListener(listener);
        holder.bind(food);
    }

    @Override
    public void onBindGroupViewHolder(MealViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.bind(group);
    }

    public class MealViewHolder extends GroupViewHolder {

        private TextView mTextViewMealType;
        private TextView mTextViewMealCalories;
        private ImageView mImageViewArrow;

        public MealViewHolder(View itemView) {
            super(itemView);
            mTextViewMealType = itemView.findViewById(R.id.textview_mealtype);
            mTextViewMealCalories = itemView.findViewById(R.id.textview_mealcalories);
            mImageViewArrow = itemView.findViewById(R.id.imageview_arrow);
        }

        public void bind(ExpandableGroup meal) {
            mTextViewMealType.setText(meal.getTitle());
            double calories = ((Meal) meal).getCalories();
            mTextViewMealCalories.setText(String.format("%.2f", calories));
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            mImageViewArrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            mImageViewArrow.setAnimation(rotate);
        }
    }

    public class FoodMainViewHolder extends ChildViewHolder {

        private TextView mTextViewFoodLabel;
        private TextView mTextViewFoodDetails;
        private TextView mTextViewFoodCalories;
        private ImageButton mImageButtonOptions;

        public FoodMainViewHolder(View itemView) {
            super(itemView);
            mTextViewFoodLabel = itemView.findViewById(R.id.textview_foodlabel);
            mTextViewFoodDetails = itemView.findViewById(R.id.textview_fooddescription);
            mTextViewFoodCalories = itemView.findViewById(R.id.textview_foodcalories);
            mImageButtonOptions = itemView.findViewById(R.id.imagebutton_options);
        }

        public ImageButton getImageButtonOptions() {
            return mImageButtonOptions;
        }

        public void bind(FirestoreFood food) {
            mTextViewFoodLabel.setText(food.getLabel());
            mTextViewFoodDetails.setText(food.getDescription());
            mTextViewFoodCalories.setText(String.format("%.2f", food.getCalories()));
        }
    }

    public interface FoodClickListener {
        void onOptionsMenuClick(View view, String foodId);
    }

}
