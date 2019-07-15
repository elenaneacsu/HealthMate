package com.elenaneacsu.healthmate.screens.logging.water;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scwang.wave.MultiWaveHeader;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.elenaneacsu.healthmate.utils.Constants.MAX_WATER;

public class LogWaterActivity extends AppCompatActivity {

    private Button mBtnInputWater;
    private CircleProgress mCircleProgress;
    private TextView mTextViewTotal;

    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;

    private int userWaterIntake;
    private long totalIntake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_water);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        initView();

        getDataFromFirestore();
//
//        mCircleProgress.setProgress((int) (100 * totalIntake / MAX_WATER));
//        mTextViewTotal.setText(getString(R.string.water_textview) + totalIntake + getString(R.string.ml));

        mBtnInputWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWaterFromUser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getDataFromFirestore();
    }

    private void initView() {
        MultiWaveHeader waveHeader = findViewById(R.id.waveHeader);
        waveHeader.isRunning();
        mBtnInputWater = findViewById(R.id.btn_inputwater);
        mCircleProgress = findViewById(R.id.circle_progress);
        mTextViewTotal = findViewById(R.id.textview_totalquantity);
    }

    private void getWaterFromUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.how_much_water_did_you_drink);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(LogWaterActivity.this, WaterAnimationActivity.class));
                userWaterIntake = Integer.valueOf(input.getText().toString());

                getDataFromFirestore();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int percentage = (int) (100 * totalIntake / MAX_WATER);
                        mCircleProgress.setProgress(percentage);
                        if (totalIntake > MAX_WATER) {
                            mCircleProgress.setProgress(100);
                        }
                        mTextViewTotal.setText(getString(R.string.water_textview) + totalIntake + getString(R.string.ml));
                        saveData();
                    }
                }, 1000);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void saveData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Map<String, Long> waterData = new HashMap<>();
        waterData.put("water", totalIntake);
        mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day))
                .update("water", totalIntake);
    }

    private void getDataFromFirestore() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                totalIntake = (long) documentSnapshot.get("water");
                totalIntake += userWaterIntake;

                mCircleProgress.setProgress((int) (100 * totalIntake / MAX_WATER));
                mTextViewTotal.setText(getString(R.string.water_textview) + totalIntake + getString(R.string.ml));            }
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
