package com.elenaneacsu.healthmate.screens.logging.food;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.SpinnerAdapter;
import com.elenaneacsu.healthmate.api.RetrofitHelper;
import com.elenaneacsu.healthmate.model.FoodDetailResponse;
import com.elenaneacsu.healthmate.model.FoodRequest;
import com.elenaneacsu.healthmate.model.Hint;
import com.elenaneacsu.healthmate.model.Ingredient;
import com.elenaneacsu.healthmate.model.Measure;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.elenaneacsu.healthmate.utils.Constants.FOOD_CLICKED;

public class FoodDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextViewFoodName;
    private Spinner mSpinner;
    private TextView mTextViewCalories;
    private EditText mEditTextCalories;
    private Button mButtonCalculateTotal;
    private ImageButton mButtonSave;
    private ConstraintLayout mLayout;

    private Hint mHint;
    private Ingredient mIngredient;
    private SpinnerAdapter mAdapter;
    private String uri;
    private String foodId;
    private double totalCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mHint = (Hint) bundle.getSerializable(FOOD_CLICKED);
            foodId = mHint.getFood().getFoodId();
            Log.d("tag", "onCreate: "+foodId);
            mTextViewFoodName.setText(mHint.getFood().getLabel());
        }

        mAdapter = new SpinnerAdapter(this, getMeasures());
        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uri = mHint.getMeasures().get(position).getUri();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mButtonCalculateTotal.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
    }

    private void initView() {
        mLayout = findViewById(R.id.constraintLayout3);
        mTextViewFoodName = findViewById(R.id.textview_foodname);
        mTextViewCalories = findViewById(R.id.textview_calories);
        mEditTextCalories = findViewById(R.id.edittext_amount);
        mSpinner = findViewById(R.id.spinner);
        mButtonCalculateTotal = findViewById(R.id.btn_calctotalcals);
        mButtonSave = findViewById(R.id.imagebutton_savedata);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_calctotalcals:
                getTotalCals();
                break;
            case R.id.imagebutton_savedata:
                finish();
                break;
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> getMeasures() {
        List<Measure> measures = new ArrayList<>(mHint.getMeasures());
        List<String> measuresLabels = new ArrayList<>();
        for(int i=0;i<measures.size();i++) {
            measuresLabels.add(measures.get(i).getLabel());
        }
        return measuresLabels;
    }

    private void getFoodNutrients(FoodRequest foodRequest) {
        RetrofitHelper.getFoodDetailResponse(foodRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<FoodDetailResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FoodDetailResponse foodDetailResponse) {
                        totalCalories = foodDetailResponse.getTotalNutrients().getEnerckal().getQuantity();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getTotalCals() {
        mIngredient = new Ingredient();
        double quantity = Double.valueOf(mEditTextCalories.getText().toString());
        mIngredient.setFoodId(foodId);
        mIngredient.setMeasureURI(uri);
        mIngredient.setQuantity(quantity);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(mIngredient);
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setIngredients(ingredients);
        getFoodNutrients(foodRequest);

        DecimalFormat df = new DecimalFormat("#.##");
        mLayout.setVisibility(View.VISIBLE);
        mTextViewCalories.setText(String.valueOf(df.format(totalCalories)));
    }
}
