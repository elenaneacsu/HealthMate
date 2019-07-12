package com.elenaneacsu.healthmate.screens.logging.weight_history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.elenaneacsu.healthmate.R;

import com.elenaneacsu.healthmate.model.WeightRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import static com.elenaneacsu.healthmate.utils.TimeUtils.stringifiedDate;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class AddWeightRecordActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.OnDataPass {
    private TextView mTextViewDate;
    private EditText mEditTextWeight;
    private Button mButtonSelectDate;
    private Button mButtonSave;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;

    private Date date;
    private float weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight_record);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initView();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        mButtonSelectDate.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_selectdate:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getSupportFragmentManager(), "Select date");
                break;
            case R.id.button_saveweight:
                if (("").equals(mEditTextWeight.getText().toString())) {
                    showToast(this, getString(com.elenaneacsu.healthmate.R.string.provide_weight));
                } else if (date == null) {
                    showToast(this, getString(com.elenaneacsu.healthmate.R.string.provide_date));
                } else {
                    weight = Float.valueOf(mEditTextWeight.getText().toString());
                    WeightRecord weightRecord = new WeightRecord();
                    weightRecord.setDate(date);
                    weightRecord.setWeight(weight);

                    mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                            .collection("weight_history").add(weightRecord);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("weight_record",weightRecord);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                break;
        }
    }

    private void initView() {
        mTextViewDate = findViewById(R.id.textview_date);
        mEditTextWeight = findViewById(R.id.edittext_weight);
        mButtonSelectDate = findViewById(R.id.button_selectdate);
        mButtonSave = findViewById(R.id.button_saveweight);
    }

    @Override
    public void onDatePass(Date date) {
        this.date = date;
        mTextViewDate.setText(stringifiedDate(date));
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
