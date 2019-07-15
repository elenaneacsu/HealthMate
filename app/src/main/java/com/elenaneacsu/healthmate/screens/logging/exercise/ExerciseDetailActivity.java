package com.elenaneacsu.healthmate.screens.logging.exercise;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.Exercise;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Calendar;

import static com.elenaneacsu.healthmate.utils.Constants.EXERCISE_CLICKED;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class ExerciseDetailActivity extends AppCompatActivity {
    private TextView mTextViewBurnedCals;
    private TextView mTextViewExercise;
    private EditText mEditTextTime;
    private ImageButton mButtonSave;

    private Exercise clickedExercise;
    private double burnedCalories;
    private double dayBurnedCalories;

    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clickedExercise = bundle.getParcelable(EXERCISE_CLICKED);
        }

        initView();

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                finish();
            }
        });
    }

    private void initView() {
        mTextViewBurnedCals = findViewById(R.id.textview_burnedcals);
        mTextViewExercise = findViewById(R.id.textview_exercisetitle);
        mTextViewExercise.setText(clickedExercise.getTitle());
        mButtonSave = findViewById(R.id.imagebutton_savedata);
        mEditTextTime = findViewById(R.id.edittext_time);
        mEditTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateBurnedCalories();
            }
        });
    }

    private void calculateBurnedCalories() {
        if (!("").equals(mEditTextTime.getText().toString())) {
            double time = Double.parseDouble(mEditTextTime.getText().toString());
            burnedCalories = clickedExercise.getCalories() / 60 * time;

        } else {
            burnedCalories = 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        mTextViewBurnedCals.setText("You burned approximately " + df.format(burnedCalories) + " calories");
    }

    private void saveData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Exercise performedExercise = new Exercise();
        performedExercise.setTitle(clickedExercise.getTitle());
        performedExercise.setCalories(burnedCalories);

        final DocumentReference docRef = mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.collection("exercise").document().set(performedExercise);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dayBurnedCalories = documentSnapshot.getDouble("burnedCalories");
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dayBurnedCalories += burnedCalories;
                docRef.update("burnedCalories", dayBurnedCalories);
            }
        }, 1000);

        showToast(this, "Exercise added!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);
    }
}
