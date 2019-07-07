package com.elenaneacsu.healthmate.screens.logging.sleep;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.elenaneacsu.healthmate.R;

public class SleepAdviceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnSelectWakeUp;
    private Button mBtnSelectGoToSleep;
    private TextView mTextViewWakeUpHour;
    private TextView mTextViewGoToSleepHour;
    private TextView mTextViewAdviceWakepUpHour;
    private TextView mTextViewAdviceGoToSleepHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_advice);

        initView();

        mBtnSelectWakeUp.setOnClickListener(this);
        mBtnSelectGoToSleep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_selectwakeup:
                TimePickerDialog firstDialog = new TimePickerDialog(SleepAdviceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (minutes == 0) {
                            mTextViewWakeUpHour.setText(hourOfDay + ":" + minutes + "0");
                        } else {
                            mTextViewWakeUpHour.setText(hourOfDay + ":" + minutes);
                        }
                        String[] result = computeBedTimeHour(hourOfDay, minutes);
                        mTextViewAdviceGoToSleepHour.setText("You should go to bed at either " + result[0]
                                + " or " + result[1]);
                    }
                }, 0, 0, false);
                firstDialog.show();
                break;
            case R.id.btn_selectgotosleep:
                TimePickerDialog secondDialog = new TimePickerDialog(SleepAdviceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (minutes == 0) {
                            mTextViewGoToSleepHour.setText(hourOfDay + ":" + minutes + "0");
                        } else {
                            mTextViewGoToSleepHour.setText(hourOfDay + ":" + minutes);
                        }
                        String[] result = computeWakeUpHour(hourOfDay, minutes);
                        mTextViewAdviceWakepUpHour.setText("You should wake up at either " + result[0]
                                + " or " + result[1]);
                    }
                }, 0, 0, false);
                secondDialog.show();
                break;

        }
    }

    private void initView() {
        mBtnSelectWakeUp = findViewById(R.id.btn_selectwakeup);
        mBtnSelectGoToSleep = findViewById(R.id.btn_selectgotosleep);
        mTextViewWakeUpHour = findViewById(R.id.textview_wuhour);
        mTextViewGoToSleepHour = findViewById(R.id.textview_gtshour);
        mTextViewAdviceWakepUpHour = findViewById(R.id.textview_advicewakeuphour);
        mTextViewAdviceGoToSleepHour = findViewById(R.id.textview_advicesleephour);
    }

    private String[] computeBedTimeHour(int hour, int minutes) {
        int maxHour;
        String maxTime;
        if (hour < 9) {
            maxHour = hour + 15;
        } else {
            maxHour = hour - 9;
        }
        if (minutes == 0) {
            maxTime = maxHour + ":" + minutes + "0";
        } else {
            maxTime = maxHour + ":" + minutes;
        }

        int minHour;
        int minMinutes;
        int auxHour = hour;
        String minTime;
        if (minutes < 30) {
            minMinutes = 30 + minutes;
            if (hour == 0) {
                auxHour = 23;
            } else {
                auxHour--;
            }
        } else {
            minMinutes = minutes - 30;
        }
        if (auxHour < 7) {
            minHour = auxHour + 17;
        } else {
            minHour = auxHour - 7;
        }

        if (minMinutes == 0) {
            minTime = minHour + ":" + minMinutes + "0";
        } else {
            minTime = minHour + ":" + minMinutes;
        }

        return new String[]{maxTime, minTime};
    }

    private String[] computeWakeUpHour(int hour, int minutes) {
        int maxHour;
        String maxTime;
        if (hour > 15) {
            maxHour = 24 - hour + 9;
        } else {
            maxHour = hour + 9;
        }
        if (minutes == 0) {
            maxTime = maxHour + ":" + minutes + "0";
        } else {
            maxTime = maxHour + ":" + minutes;
        }

        int minHour;
        int minMinutes;
        int auxHour = hour;
        String minTime;
        if (minutes < 30) {
            minMinutes = 30 + minutes;
            auxHour++;
        } else {
            minMinutes = minutes + 30;
        }
        if (auxHour > 17) {
            minHour = 24 - auxHour + 7;
        } else {
            minHour = auxHour + 7;
        }

        if (minMinutes == 0) {
            minTime = minHour + ":" + minMinutes + "0";
        } else {
            minTime = minHour + ":" + minMinutes;
        }

        return new String[]{minTime, maxTime};
    }

}
