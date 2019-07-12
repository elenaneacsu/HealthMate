package com.elenaneacsu.healthmate.screens.logging.weight_history;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.WeightAdapter;
import com.elenaneacsu.healthmate.model.WeightRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elenaneacsu.healthmate.utils.TimeUtils.stringifiedDate;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeightHistoryFragment extends Fragment {

    private Button mButtonAddEntry;
    private WeightAdapter mAdapter;
    private LineChart mLineChart;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private List<WeightRecord> mWeightRecordList;

    public WeightHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeightRecordList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mLineChart = view.findViewById(R.id.weight_chart);
        mButtonAddEntry = view.findViewById(R.id.btn_addentry);

        getDataFromFirestore();
        setUpRecyclerView(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpChart();
            }
        }, 1000);

        mButtonAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWeightRecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if(resultCode == Activity.RESULT_OK) {
                WeightRecord weightRecord = data.getParcelableExtra("weight_record");
                mWeightRecordList.add(weightRecord);
                mAdapter.notifyDataSetChanged();
                setUpChart();
            }
        }
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new WeightAdapter(mWeightRecordList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void setUpChart() {
        List<Entry> vals = new ArrayList<>();
        List<Float> weights = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for(WeightRecord weight : mWeightRecordList) {
            weights.add(weight.getWeight());
            dates.add(stringifiedDate(weight.getDate()));
        }
        for (int i = 0;i<weights.size();i++) {
            vals.add(new Entry(i, weights.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(vals, "Weight");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(R.color.colorAccent);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate();


    }

    private void getDataFromFirestore() {
        mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("weight_history").orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documment : task.getResult()) {
                                WeightRecord weightRecord = documment.toObject(WeightRecord.class);
                                mWeightRecordList.add(weightRecord);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showToast(getActivity().getApplicationContext(), "Error while getting data");
                        }
                    }
                });
    }
}
