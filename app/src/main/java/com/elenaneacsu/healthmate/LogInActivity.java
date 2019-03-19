package com.elenaneacsu.healthmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void goToSignUp(View view) {
        startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
    }
}
