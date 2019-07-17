package com.elenaneacsu.healthmate.screens.statistics;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.SpinnerAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {
    private SpinnerAdapter mAdapter;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private String type;

    private PieChart mPieChart;
    private LineChart mLineChart;
    private TextView mTextViewCarbs;
    private TextView mTextViewProtein;
    private TextView mTextViewFat;
    private TextView mTextViewAdvice;
    private Spinner mSpinner;

    private long fat;
    private long protein;
    private long carbs;
    private List<Long> weeklyCalories;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        weeklyCalories = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        mAdapter = new SpinnerAdapter(getContext(), getTypes());
        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = getTypes().get(position);
                switch (type) {
                    case "Daily Nutrients":
                        getDailyNutrients();
                        break;
                    case "Weekly Nutrients":
                        getWeeklyNutrients();
                        break;
                    case "Weekly Calories":
                        getWeeklyCalories();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView(View view) {
        mPieChart = view.findViewById(R.id.pie_chart);
        mLineChart = view.findViewById(R.id.line_chart);
        mSpinner = view.findViewById(R.id.spinner);
        mTextViewCarbs = view.findViewById(R.id.textview_carbs);
        mTextViewProtein = view.findViewById(R.id.textview_protein);
        mTextViewFat = view.findViewById(R.id.textview_fat);
        mTextViewAdvice = view.findViewById(R.id.textview_advice);
    }

    private List<String> getTypes() {
        List<String> types = new ArrayList<>();
        types.add("Daily Nutrients");
        types.add("Weekly Nutrients");
        types.add("Weekly Calories");
        return types;
    }

    private void getDailyNutrients() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        CollectionReference colRef = mFirebaseFirestore.collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats");
        final DocumentReference docRef = colRef.document(String.valueOf(year))
                .collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        carbs = (long) documentSnapshot.get("carbs");
                        protein = (long) documentSnapshot.get("protein");
                        fat = (long) documentSnapshot.get("fat");
                    }
                    initPieChart("Today's Nutrients");
                    setTextForTextview();
                }
            }
        });
    }

    private void getWeeklyNutrients() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        carbs = 0;
        fat = 0;
        protein = 0;
        getNutrients(year, month, day);

        int i = 0;
        while (i < 6) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            getNutrients(year, month, day);
            i++;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initPieChart("Weekly Nutrients");
                setTextForTextview();
            }
        }, 1000);

    }

    private void getNutrients(int year, int month, int day) {
        CollectionReference colRef = mFirebaseFirestore.collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats");
        final DocumentReference docRef = colRef.document(String.valueOf(year))
                .collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        carbs += (long) documentSnapshot.get("carbs");
                        protein += (long) documentSnapshot.get("protein");
                        fat += (long) documentSnapshot.get("fat");
                    }
                }
            }
        });
    }

    private void getWeeklyCalories() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        getCalories(year, month, day);

        int i = 0;
        while (i < 6) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            getCalories(year, month, day);
            i++;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initLineChart();
            }
        }, 1000);
    }

    private void getCalories(int year, int month, int day) {
        CollectionReference colRef = mFirebaseFirestore.collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats");
        final DocumentReference docRef = colRef.document(String.valueOf(year))
                .collection(String.valueOf(month)).document(String.valueOf(day));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Log.d("tag", "onComplete: ");
                        weeklyCalories.add((long) documentSnapshot.get("eatenCalories") - (long) documentSnapshot.get("burnedCalories"));
                    }
                }
            }
        });
    }

    private void initPieChart(String label) {
        mPieChart.setVisibility(View.VISIBLE);
        mLineChart.setVisibility(View.GONE);
        mTextViewCarbs.setVisibility(View.VISIBLE);
        mTextViewProtein.setVisibility(View.VISIBLE);
        mTextViewFat.setVisibility(View.VISIBLE);
        mTextViewAdvice.setVisibility(View.VISIBLE);

        mPieChart.setUsePercentValues(true);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(carbs, "Carbs"));
        entries.add(new PieEntry(protein, "Protein"));
        entries.add(new PieEntry(fat, "Fat"));

        PieDataSet set = new PieDataSet(entries, label);
        set.setColors(Color.rgb(244, 128, 36), Color.rgb(168, 149, 242),
                Color.rgb(164, 198, 57));
        PieData data = new PieData(set);

        mPieChart.setData(data);
        mPieChart.invalidate();
    }

    private void initLineChart() {
        mLineChart.setVisibility(View.VISIBLE);
        mPieChart.setVisibility(View.GONE);
        mTextViewCarbs.setVisibility(View.GONE);
        mTextViewProtein.setVisibility(View.GONE);
        mTextViewFat.setVisibility(View.GONE);
        mTextViewAdvice.setVisibility(View.GONE);

        mLineChart.setPinchZoom(true);

        List<Entry> vals = new ArrayList<>();
        List<Long> calories = new ArrayList<>(weeklyCalories);
        for(int i=0;i<calories.size();i++) {
            vals.add(new Entry(i, calories.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(vals, "Net Calories");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(Color.rgb(244, 128, 36));

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    private void setTextForTextview() {
        mTextViewCarbs.setText("Carbs: " + String.valueOf(carbs) + "g");
        mTextViewProtein.setText("Protein: " + String.valueOf(protein) + "g");
        mTextViewFat.setText("Fat: " + String.valueOf(fat) + "g");
    }
}
