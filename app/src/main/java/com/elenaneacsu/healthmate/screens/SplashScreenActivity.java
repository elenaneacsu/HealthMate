package com.elenaneacsu.healthmate.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import com.elenaneacsu.healthmate.R;

import com.elenaneacsu.healthmate.screens.login.LogInActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private SplashScreenAsyncTask splashScreenAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        splashScreenAsyncTask = new SplashScreenAsyncTask();
        splashScreenAsyncTask.execute();
    }

    private class SplashScreenAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (splashScreenAsyncTask != null) {
            splashScreenAsyncTask.cancel(true);
        }
        super.onDestroy();
    }
}
