package com.elenaneacsu.healthmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.Exercise;
import com.elenaneacsu.healthmate.screens.logging.exercise.ExerciseDetailActivity;

import java.util.List;

import static com.elenaneacsu.healthmate.utils.Constants.EXERCISE_CLICKED;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> mExerciseList;
    private Context mContext;

    public ExerciseAdapter(List<Exercise> exerciseList) {
        mExerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View itemView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.exercise_item, viewGroup, false);
        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int i) {
        Exercise exercise = mExerciseList.get(i);
        if (exercise != null) {
            exerciseViewHolder.mExercise = exercise;
            exerciseViewHolder.mTextViewCalories.setText(String.valueOf(exercise.getCalories()));
            exerciseViewHolder.mTextViewTitle.setText(exercise.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mExerciseList.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewCalories;
        private Exercise mExercise;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.textview_exercisetitle);
            mTextViewCalories = itemView.findViewById(R.id.textview_burnedcalories);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ExerciseDetailActivity.class);
                    intent.putExtra(EXERCISE_CLICKED, mExercise);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
