package com.elenaneacsu.healthmate.screens.logging.sleep;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.yinglan.circleviewlibrary.CircleAlarmTimerView;

public class LogSleepActivity extends AppCompatActivity {

    private CircleAlarmTimerView mTimerView;
    private TextView mTextViewBedtime;
    private TextView mTextViewWakeup;
    private Button mBtnSave;

    private String bedtime;
    private String wakeup;
    private int hoursSlept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
                Log.d("test", "onClick: "+hoursSlept);
            }
        });
    }

    private void initView() {
        mTimerView = findViewById(R.id.timer_view);
        mTextViewBedtime = findViewById(R.id.textview_bedtime);
        mTextViewWakeup = findViewById(R.id.textview_wakeup);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
