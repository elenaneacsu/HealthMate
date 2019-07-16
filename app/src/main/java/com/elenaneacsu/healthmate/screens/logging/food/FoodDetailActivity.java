package com.elenaneacsu.healthmate.screens.logging.food;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.elenaneacsu.healthmate.model.FirestoreFood;
import com.elenaneacsu.healthmate.model.FoodDetailResponse;
import com.elenaneacsu.healthmate.model.FoodRequest;
import com.elenaneacsu.healthmate.model.Hint;
import com.elenaneacsu.healthmate.model.Ingredient;
import com.elenaneacsu.healthmate.model.Measure;
import com.elenaneacsu.healthmate.screens.MainActivity;
import com.elenaneacsu.healthmate.screens.profile.EditProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.elenaneacsu.healthmate.utils.Constants.FOOD_CLICKED;
import static com.elenaneacsu.healthmate.utils.Constants.FRAGMENT;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class FoodDetailActivity extends AppCompatActivity implements View.OnClickListener {

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

    private long totalCaloriesPerFood;
    private long protein;
    private long fat;
    private long carbs;
    private long dayEatenCals;
    private long dayCarbs;
    private long dayProtein;
    private long dayFat;
    private String mealType;

    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mHint = (Hint) bundle.getSerializable(FOOD_CLICKED);
            foodId = mHint.getFood().getFoodId();
            mTextViewFoodName.setText(mHint.getFood().getLabel());
            mealType = bundle.getString("meal");
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
        switch (v.getId()) {
            case R.id.btn_calctotalcals:
                getTotalCals();
                break;
            case R.id.imagebutton_savedata:
                saveData();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    private List<String> getMeasures() {
        List<Measure> measures = new ArrayList<>(mHint.getMeasures());
        List<String> measuresLabels = new ArrayList<>();
        for (int i = 0; i < measures.size(); i++) {
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
                        if (foodDetailResponse.getTotalNutrients().getEnerckal() != null) {
                            totalCaloriesPerFood = Math.round(foodDetailResponse.getTotalNutrients().getEnerckal().getQuantity());
                        } else {
                            totalCaloriesPerFood = 0;
                        }
                        if (foodDetailResponse.getTotalNutrients().getProcnt() != null) {
                            protein = Math.round(foodDetailResponse.getTotalNutrients().getProcnt().getQuantity());
                        } else {
                            protein = 0;
                        }
                        if (foodDetailResponse.getTotalNutrients().getFat() != null) {
                            fat = Math.round(foodDetailResponse.getTotalNutrients().getFat().getQuantity());
                        } else {
                            fat = 0;
                        }
                        if (foodDetailResponse.getTotalNutrients().getChocdf() != null) {
                            carbs = Math.round(foodDetailResponse.getTotalNutrients().getChocdf().getQuantity());
                        } else {
                            carbs = 0;
                        }
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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayout.setVisibility(View.VISIBLE);
                mTextViewCalories.setText(String.valueOf(totalCaloriesPerFood));
            }
        }, 500);

    }

    private void saveData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        FirestoreFood fsFood = new FirestoreFood();
        fsFood.setCalories(totalCaloriesPerFood);
        fsFood.setLabel(mHint.getFood().getLabel());
        fsFood.setDescription(mHint.getFood().getBrand());
        fsFood.setProtein(protein);
        fsFood.setCarbs(carbs);
        fsFood.setFat(fat);

        final DocumentReference docRef = mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.collection(mealType).document().set(fsFood);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dayEatenCals = (long) documentSnapshot.get("eatenCalories");
                dayCarbs = (long) documentSnapshot.get("carbs");
                dayProtein = (long) documentSnapshot.get("protein");
                dayFat = (long) documentSnapshot.get("fat");
                dayEatenCals += totalCaloriesPerFood;
                dayCarbs += carbs;
                dayFat += fat;
                dayProtein += protein;
                docRef.update("eatenCalories", dayEatenCals);
                docRef.update("carbs", dayCarbs);
                docRef.update("fat", dayFat);
                docRef.update("protein", dayProtein);
            }
        });
        showToast(this, "Food added!");
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);
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
