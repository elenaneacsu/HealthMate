package com.elenaneacsu.healthmate.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.github.lzyzsd.circleprogress.CircleProgress;

import static com.elenaneacsu.healthmate.utils.Constants.MAX_WATER;

public class LogWaterActivity extends AppCompatActivity {

    private Button mBtnInputWater;
    private CircleProgress mCircleProgress;
    private TextView mTextViewTotal;
    private static int userWater = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_water);

        initView();
        mCircleProgress.setProgress(100*userWater/MAX_WATER);
        mTextViewTotal.setText(getString(R.string.water_textview)+userWater+getString(R.string.ml));

        mBtnInputWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWaterFromUser();
            }
        });
    }

    private void initView() {
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
                userWater+=Integer.valueOf(input.getText().toString());
                final int percentage = 100*userWater/MAX_WATER;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCircleProgress.setProgress(percentage);
                        mTextViewTotal.setText(getString(R.string.water_textview)+userWater+getString(R.string.ml));
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
}