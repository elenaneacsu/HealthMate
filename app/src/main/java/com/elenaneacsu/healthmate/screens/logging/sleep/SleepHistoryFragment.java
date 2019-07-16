package com.elenaneacsu.healthmate.screens.logging.sleep;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.SleepAdapter;
import com.elenaneacsu.healthmate.model.SleepRecord;
import com.elenaneacsu.healthmate.model.WaterRecord;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleepHistoryFragment extends Fragment {
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;
    private SleepAdapter mAdapter;
    private BarChart mBarChart;

    private List<SleepRecord> mSleepRecordList;

    public SleepHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep_history, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSleepRecordList = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBarChart = view.findViewById(R.id.sleep_history);

        getDataFromFirestore();
        setUpRecyclerView(view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpChart();
            }
        }, 500);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SleepAdapter(mSleepRecordList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void getDataFromFirestore() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(String.valueOf(year))
                .collection(String.valueOf(month)).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                long hoursOfSleep = document.getLong("hoursOfSleep");
                                String day = document.getId();
                                calendar.set(year, month - 1, Integer.valueOf(day));
                                Date date = calendar.getTime();
                                mSleepRecordList.add(new SleepRecord(hoursOfSleep, date));
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void setUpChart() {
        List<BarEntry> vals = new ArrayList<>();
        List<Long> sleepRecords = new ArrayList<>();
        for(int i=0;i<mSleepRecordList.size();i++) {
            sleepRecords.add(mSleepRecordList.get(i).getHours());
        }
        for (int i = 0; i < sleepRecords.size(); i++) {
            vals.add(new BarEntry(i, sleepRecords.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(vals, "Hours of sleep");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(R.color.colorAccent);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBorders(false);

        BarData data = new BarData(dataSets);
        mBarChart.setData(data);
        mBarChart.invalidate();
    }
}
