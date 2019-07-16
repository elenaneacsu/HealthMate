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
import android.widget.ImageButton;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.elenaneacsu.healthmate.utils.CaloriesUtils.calculateGoalCalories;
import static com.elenaneacsu.healthmate.utils.TimeUtils.stringifiedDate;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MealAdapter.FoodClickListener {

    private RecyclerView mRecyclerView;
    private TextView mTextViewCalsEaten;
    private TextView mTextViewCalsBurned;
    private TextView mTextViewGoalCals;
    private TextView mTextViewCarbs;
    private TextView mTextViewProtein;
    private TextView mTextViewFat;
    private TextView mTextViewDate;
    private ImageButton mButtonBack;
    private ImageButton mButtonForward;
    private CircularProgressBar mCircularProgressBar;

    private long goalCalories;
    private long eatenCalories;
    private long burnedCalories;
    private long netCalories;
    private long carbs;
    private long protein;
    private long fat;

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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkDate();

        initView(view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        setUpAdapter(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        setUpAdapter(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        setUpAdapter(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        setUpAdapter(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        mMealAdapter = new MealAdapter(meals);
        mRecyclerView.setAdapter(mMealAdapter);

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPreviousDay();
            }
        });


        mButtonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextDay();
            }
        });
    }


    private void getMealsFromFirestore(final String meal, int year, int month, int day) {
        DocumentReference docRef = mFirestore.collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year))
                .collection(String.valueOf(month)).document(String.valueOf(day));
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
                                    breakfastFoodList.clear();
                                    breakfastFoodList.addAll(temporaryList);
                                    break;
                                case "lunch":
                                    lunchFoodList.clear();
                                    lunchFoodList.addAll(temporaryList);
                                    break;
                                case "snack":
                                    snackFoodList.clear();
                                    snackFoodList.addAll(temporaryList);
                                    break;
                                case "dinner":
                                    dinnerFoodList.clear();
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
        mRecyclerView = view.findViewById(R.id.recyclerview_fooditems);
        mTextViewCalsEaten = view.findViewById(R.id.textview_calseaten);
        mTextViewCalsBurned = view.findViewById(R.id.textview_calsburned);
        mTextViewGoalCals = view.findViewById(R.id.textview_goalcalories);
        mTextViewCarbs = view.findViewById(R.id.textview_carbs);
        mTextViewProtein = view.findViewById(R.id.textview_protein);
        mTextViewFat = view.findViewById(R.id.textview_fat);
        mTextViewDate = view.findViewById(R.id.textview_date);
        mCircularProgressBar = view.findViewById(R.id.progressbar);
        mButtonBack = view.findViewById(R.id.button_back);
        mButtonForward = view.findViewById(R.id.button_forward);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onResume() {
        super.onResume();
        checkDate();
    }

    private void checkDate() {
        final Calendar calendar = Calendar.getInstance();
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
                        eatenCalories = (long) documentSnapshot.get("eatenCalories");
                        burnedCalories = (long) documentSnapshot.get("burnedCalories");
                        carbs = (long) documentSnapshot.get("carbs");
                        protein = (long) documentSnapshot.get("protein");
                        fat = (long) documentSnapshot.get("fat");
                        setViews();
                        mTextViewDate.setText(stringifiedDate(calendar.getTime()));
                        if(!stringifiedDate(calendar.getTime()).equals("")) {
                            mButtonForward.setVisibility(View.INVISIBLE);
                        }
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
                                Map<String, Long> data = new HashMap<>();
                                data.put("eatenCalories", eatenCalories);
                                data.put("burnedCalories", burnedCalories);
                                data.put("carbs", carbs);
                                data.put("protein", protein);
                                data.put("fat", fat);
                                data.put("goalCalories", goalCalories);
                                data.put("water", 0L);

                                docRef.set(data);
                                setViews();
                            }
                        }, 500);
                        mTextViewDate.setText(stringifiedDate(calendar.getTime()));
                    }
                }
            }
        });
    }

    private void setViews() {
        mTextViewCalsEaten.setText(String.valueOf(eatenCalories));
        mTextViewGoalCals.setText(String.valueOf(goalCalories));
        mTextViewCalsBurned.setText(String.valueOf(burnedCalories));
        mTextViewCarbs.setText(String.valueOf(carbs));
        mTextViewProtein.setText(String.valueOf(protein));
        mTextViewFat.setText(String.valueOf(fat));
        mCircularProgressBar.setProgress((float) (eatenCalories - burnedCalories));
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

    private void getPreviousDay() {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        if (mTextViewDate.getText().toString().equalsIgnoreCase(stringifiedDate(calendar.getTime()))) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            String stringDate = mTextViewDate.getText().toString();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
//            DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
            Date date = new Date();
            try {
                date = dateFormat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        final int newDay, newMonth, newYear;

        if (month == 3 || month == 5 || month == 7 || month == 10 || month == 12) {
            if (day == 1) {
                newDay = 30;
                newMonth = month - 1;
            } else {
                newDay = day - 1;
                newMonth = month;
            }
            newYear = year;
        } else if (month == 1 && day == 1) {
            newYear = year - 1;
            newMonth = 12;
            newDay = 31;
        } else {
            if (day == 1) {
                newDay = 31;
                newMonth = month - 1;
            } else {
                newDay = day - 1;
                newMonth = month;
            }
            newYear = year;
        }

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(newYear, newMonth - 1, newDay);
        final Date newDate = new Date();
        newDate.setTime(newCalendar.getTimeInMillis());

        setUpAdapter(newYear, newMonth, newDay);
        mMealAdapter.notifyDataSetChanged();

        getNutrients(newYear, newMonth, newDay, newDate);

        mButtonForward.setVisibility(View.VISIBLE);
    }

    private void getNextDay() {
        String stringDate = mTextViewDate.getText().toString();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int newDay, newMonth, newYear;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10) {
            if (day == 31) {
                newDay = 1;
                newMonth = month + 1;
            } else {
                newDay = day + 1;
                newMonth = month;
            }
            newYear = year;
        } else if (month == 12 && day == 31) {
            newDay = 1;
            newMonth = 1;
            newYear = year + 1;
        } else {
            if (day == 30) {
                newDay = 1;
                newMonth = month + 1;
            } else {
                newDay = day + 1;
                newMonth = month;
            }
            newYear = year;
        }

        setUpAdapter(newYear, newMonth, newDay);
        mMealAdapter.notifyDataSetChanged();

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(newYear, newMonth - 1, newDay);
        final Date newDate = newCalendar.getTime();

        getNutrients(newYear, newMonth, newDay, newDate);

        if (dateFormat.format(newCalendar.getTime()).equalsIgnoreCase(dateFormat.format(Calendar.getInstance().getTime()))) {
            mButtonForward.setVisibility(View.INVISIBLE);
        }

    }

    private void setUpAdapter(int year, int month, int day) {
        getMealsFromFirestore("breakfast", year, month, day);
        getMealsFromFirestore("lunch", year, month, day);
        getMealsFromFirestore("snack", year, month, day);
        getMealsFromFirestore("dinner", year, month, day);
        meals.clear();
        meals.add(new Meal("breakfast", breakfastFoodList));
        meals.add(new Meal("lunch", lunchFoodList));
        meals.add(new Meal("snack", snackFoodList));
        meals.add(new Meal("dinner", dinnerFoodList));
    }

    private void getNutrients(int year, int month, int day, final Date date) {
        CollectionReference colRef = mFirestore.collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid()).collection("stats");
        final DocumentReference docRef = colRef.document(String.valueOf(year))
                .collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        goalCalories = (long) documentSnapshot.get("goalCalories");
                        eatenCalories = (long) documentSnapshot.get("eatenCalories");
                        burnedCalories = (long) documentSnapshot.get("burnedCalories");
                        carbs = (long) documentSnapshot.get("carbs");
                        protein = (long) documentSnapshot.get("protein");
                        fat = (long) documentSnapshot.get("fat");
                        setViews();

                        mTextViewDate.setText(stringifiedDate(date));
                    }
                }
            }
        });
    }

    @Override
    public void onOptionsMenuClick(View view, String foodId) {

    }
}
