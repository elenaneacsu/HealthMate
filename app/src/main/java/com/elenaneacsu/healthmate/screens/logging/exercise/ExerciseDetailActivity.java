package com.elenaneacsu.healthmate.screens.logging.exercise;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.Exercise;

import java.text.DecimalFormat;

import static com.elenaneacsu.healthmate.utils.Constants.EXERCISE_CLICKED;

public class ExerciseDetailActivity extends AppCompatActivity {
    private TextView mTextViewBurnedCals;
    private TextView mTextViewExercise;
    private EditText mEditTextTime;
    private ImageButton mButtonSave;

    private Exercise clickedExercise;
    private float burnedCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clickedExercise = bundle.getParcelable(EXERCISE_CLICKED);
        }

        initView();

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE TO DB
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
        if(!("").equals(mEditTextTime.getText().toString())) {
            float time = Float.parseFloat(mEditTextTime.getText().toString());
            burnedCalories = clickedExercise.getCalories() / 60 * time;

        } else {
            burnedCalories = 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        mTextViewBurnedCals.setText("You burned approximately " + df.format(burnedCalories) + " calories");
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
