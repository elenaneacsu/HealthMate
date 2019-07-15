package com.elenaneacsu.healthmate.screens.logging.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yinglan.circleviewlibrary.CircleAlarmTimerView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class LogSleepActivity extends AppCompatActivity {

    private CircleAlarmTimerView mTimerView;
    private TextView mTextViewBedtime;
    private TextView mTextViewWakeup;
    private TextView mTextViewSleepAdvice;
    private Button mBtnSave;

    private String bedtime;
    private String wakeup;
    private int hoursSlept;

    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        initView();

        mTimerView.setOnTimeChangedListener(new CircleAlarmTimerView.OnTimeChangedListener() {
            @Override
            public void start(String starting) {
                bedtime = starting;
                mTextViewBedtime.setText(starting);
            }

            @Override
            public void end(String ending) {
                wakeup = ending;
                mTextViewWakeup.setText(ending);
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoursSlept = computeTimeDiff(bedtime, wakeup);
                saveData();
            }
        });

        mTextViewSleepAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogSleepActivity.this, SleepAdviceActivity.class));
            }
        });
    }

    private void initView() {
        mTimerView = findViewById(R.id.timer_view);
        mTextViewBedtime = findViewById(R.id.textview_bedtime);
        mTextViewWakeup = findViewById(R.id.textview_wakeup);
        mTextViewSleepAdvice = findViewById(R.id.textview_sleepadvice);
        mBtnSave = findViewById(R.id.btn_save);
    }

    private int computeTimeDiff(String bedtimeHour, String wakeupHour) {
        int hour1 = Integer.parseInt(bedtimeHour.substring(0, 2));
        int min1 = Integer.parseInt(bedtimeHour.substring(3));
        int hour2 = Integer.parseInt(wakeupHour.substring(0, 2));
        int min2 = Integer.parseInt(wakeupHour.substring(3));
        int diffMin;
        if (min2 < min1) {
            diffMin = 60 + min2 - min1;
            if (hour2 == 0) {
                hour2 = 23;
            } else {
                hour2--;
            }
        } else {
            diffMin = min2 - min1;
        }

        int diffHour;
        if (hour2 < hour1) {
            diffHour = hour2 + 24 - hour1;
        } else {
            diffHour = hour2 - hour1;
        }
        if(diffMin>30) {
            diffHour++;
        }
        return diffHour;
    }

    private void saveData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day))
                .update("hoursOfSleep", hoursSlept);

        showToast(this, "Sleep hours saved!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
