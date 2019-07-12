package com.elenaneacsu.healthmate.screens.logging.exercise;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import com.elenaneacsu.healthmate.R;

import com.elenaneacsu.healthmate.adapter.ExerciseAdapter;
import com.elenaneacsu.healthmate.model.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class LogExerciseActivity extends AppCompatActivity {
    private List<Exercise> mExerciseList;
    private FirebaseFirestore mFirestore;
    ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_exercise);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mExerciseList = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        exerciseAdapter = new ExerciseAdapter(mExerciseList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(exerciseAdapter);
    }

    private void getDataFromFirestore() {
        mFirestore.collection("exercise").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Exercise exercise = document.toObject(Exercise.class);
                                mExerciseList.add(exercise);
                            }
                            exerciseAdapter.notifyDataSetChanged();
                        } else {
                            showToast(getApplicationContext(), "Error while getting data");
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
