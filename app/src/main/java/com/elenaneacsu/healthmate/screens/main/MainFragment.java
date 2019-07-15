package com.elenaneacsu.healthmate.screens.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.MealAdapter;
import com.elenaneacsu.healthmate.model.FirestoreFood;
import com.elenaneacsu.healthmate.model.Meal;
import com.elenaneacsu.healthmate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.elenaneacsu.healthmate.utils.CaloriesUtils.calculateGoalCalories;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MealAdapter.FoodClickListener {

    private RecyclerView mRecyclerViewFoodStatus;
    private TextView mTextViewCalsEaten;
    private TextView mTextViewCalsBurned;
    private TextView mTextViewGoalCals;
    private TextView mTextViewCarbs;
    private TextView mTextViewProtein;
    private TextView mTextViewFat;
    private TextView mTextViewDate;
    private CircularProgressBar mCircularProgressBar;

    private long goalCalories;
    private double eatenCalories;
    private double burnedCalories;
    private double netCalories;
    private double carbs;
    private double protein;
    private double fat;

    private MealAdapter mMealAdapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private List<FirestoreFood> breakfastFoodList = new ArrayList<>();
    private List<FirestoreFood> lunchFoodList = new ArrayList<>();
    private List<FirestoreFood> snackFoodList = new ArrayList<>();
    private List<FirestoreFood> dinnerFoodList = new ArrayList<>();
    private List<Meal> meals = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initView(view);
        checkDate();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewFoodStatus.setLayoutManager(layoutManager);
        mRecyclerViewFoodStatus.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        getMealsFromFirestore("breakfast");
        getMealsFromFirestore("lunch");
        getMealsFromFirestore("snack");
        getMealsFromFirestore("dinner");
        meals.add(new Meal("breakfast", breakfastFoodList));
        meals.add(new Meal("lunch", lunchFoodList));
        meals.add(new Meal("snack", snackFoodList));
        meals.add(new Meal("dinner", dinnerFoodList));

        mMealAdapter = new MealAdapter(meals);
        mRecyclerViewFoodStatus.setAdapter(mMealAdapter);

        Log.d("tag", "onSuccess: onCreateView: ");

        return view;
    }



    private void getMealsFromFirestore(final String meal) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DocumentReference docRef = mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.collection(meal).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<FirestoreFood> temporaryList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirestoreFood food = document.toObject(FirestoreFood.class);
                                temporaryList.add(food);
                            }
                            switch (meal) {
                                case "breakfast":
                                    breakfastFoodList.addAll(temporaryList);
                                    break;
                                case "lunch":
                                    lunchFoodList.addAll(temporaryList);
                                    break;
                                case "snack":
                                    snackFoodList.addAll(temporaryList);
                                    break;
                                case "dinner":
                                    dinnerFoodList.addAll(temporaryList);
                                    break;
                            }
                            mMealAdapter.notifyDataSetChanged();
                        } else {
                            showToast(getContext(), "error");
                        }
                    }
                });
    }

    private void initView(View view) {
        mRecyclerViewFoodStatus = view.findViewById(R.id.recyclerview_fooditems);
        mTextViewCalsEaten = view.findViewById(R.id.textview_calseaten);
        mTextViewCalsBurned = view.findViewById(R.id.textview_calsburned);
        mTextViewGoalCals = view.findViewById(R.id.textview_goalcalories);
        mTextViewCarbs = view.findViewById(R.id.textview_carbs);
        mTextViewProtein = view.findViewById(R.id.textview_protein);
        mTextViewFat = view.findViewById(R.id.textview_fat);
        mTextViewDate = view.findViewById(R.id.textview_date);
        mCircularProgressBar = view.findViewById(R.id.progressbar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkDate();
    }

    private void checkDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        CollectionReference colRef = mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid()).collection("stats");
        final DocumentReference docRef = colRef.document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        goalCalories = (long) documentSnapshot.get("goalCalories");
                        eatenCalories = (double) documentSnapshot.get("eatenCalories");
                        burnedCalories = (double) documentSnapshot.get("burnedCalories");
                        carbs = (double) documentSnapshot.get("carbs");
                        protein = (double) documentSnapshot.get("protein");
                        fat = (double) documentSnapshot.get("fat");
                        setViews();
                        mCircularProgressBar.setProgress((float) (eatenCalories-burnedCalories));
                    } else {
                        calcGoalCalories();
                        eatenCalories = 0;
                        burnedCalories = 0;
                        carbs = 0;
                        protein = 0;
                        fat = 0;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, Double> data = new HashMap<>();
                                data.put("eatenCalories", eatenCalories);
                                data.put("burnedCalories", burnedCalories);
                                data.put("carbs", carbs);
                                data.put("protein", protein);
                                data.put("fat", fat);

                                docRef.set(data);
                                docRef.update("goalCalories", goalCalories);
                                docRef.update("water", 0L);
                                setViews();
                                mCircularProgressBar.setProgress((float) (eatenCalories-burnedCalories));
                            }
                        }, 500);

                    }
                }
            }
        });
    }

    private void setViews() {
        DecimalFormat df = new DecimalFormat("#.#");
        mTextViewCalsEaten.setText(String.valueOf(df.format(eatenCalories)));
        mTextViewGoalCals.setText(String.valueOf(df.format(goalCalories)));
        mTextViewCalsBurned.setText(String.valueOf(df.format(burnedCalories)));
        mTextViewCarbs.setText(String.valueOf(df.format(carbs)));
        mTextViewProtein.setText(String.valueOf(df.format(protein)));
        mTextViewFat.setText(String.valueOf(df.format(fat)));
    }

    private void calcGoalCalories() {
        mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        goalCalories = calculateGoalCalories(user);
                    }
                });
    }

    @Override
    public void onOptionsMenuClick(View view, String foodId) {

    }
}
