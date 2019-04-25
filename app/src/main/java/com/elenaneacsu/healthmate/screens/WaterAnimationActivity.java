package com.elenaneacsu.healthmate.screens;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.elenaneacsu.healthmate.R;

public class WaterAnimationActivity extends AppCompatActivity {

    private ImageView mImageViewAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_animation);

        mImageViewAnimation = findViewById(R.id.imageview_animation);
        mImageViewAnimation.setBackgroundResource(R.drawable.filling_glass_animation);
        ((AnimationDrawable) mImageViewAnimation.getBackground()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
