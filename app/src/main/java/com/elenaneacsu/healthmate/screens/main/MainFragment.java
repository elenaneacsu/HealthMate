package com.elenaneacsu.healthmate.screens.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.MealAdapter;
import com.elenaneacsu.healthmate.model.RegisteredDay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private RecyclerView mRecyclerViewFoodStatus;
    private TextView mTextViewCalsEaten;
    private TextView mTextViewCalsBurned;
    private TextView mTextViewGoalCals;
    private TextView mTextViewDate;
    private CircularProgressBar mCircularProgressBar;

    private List<RegisteredDay> mRegisteredDays = new ArrayList<>();
    private RegisteredDay mDay;
    private MealAdapter mMealAdapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerViewFoodStatus = view.findViewById(R.id.recyclerview_fooditems);
        mTextViewCalsEaten = view.findViewById(R.id.textview_calseaten);
        mTextViewCalsBurned = view.findViewById(R.id.textview_calsburned);
        mTextViewGoalCals = view.findViewById(R.id.textview_goalcalories);
        mTextViewDate = view.findViewById(R.id.textview_date);
        mCircularProgressBar = view.findViewById(R.id.progressbar);

        getDaysFromFirestore();

        return view;
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

    private void getDaysFromFirestore() {
        Calendar calendar = Calendar.getInstance();
        String dateString = "16.0"+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.YEAR);
//        mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
//                .collection("stats").document(dateString).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
//                Log.d("TAG", "onComplete: " + myListOfDocuments);
//                task.getResult().getDocuments().get(0).getId();
//                mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
//                        .collection("stats");
//            }
//        });

        mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(dateString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                task.getResult().get("goalCalories");
            }
        });

        mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("stats").document(dateString).collection("breakfast").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                task.getResult().getDocuments();
            }
        });
    }
}
