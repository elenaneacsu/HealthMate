package com.elenaneacsu.healthmate.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.Hint;

import static com.elenaneacsu.healthmate.utils.Constants.FOOD_CLICKED;

public class FoodDetailActivity extends AppCompatActivity {

    private TextView mTextViewFoodName;

    private Hint mHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        initView();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            mHint = (Hint) bundle.getSerializable(FOOD_CLICKED);
            if(mHint==null) {
                Log.d("tag", "e null hintul");
            } else {
                mTextViewFoodName.setText(mHint.getFood().getLabel());
            }
        }

    }

    private void initView() {
        mTextViewFoodName = findViewById(R.id.textview_foodname);
    }
}
