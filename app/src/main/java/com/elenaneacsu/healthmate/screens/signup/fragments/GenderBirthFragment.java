package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.elenaneacsu.healthmate.utils.Constants.SELECTED_DATE;
import static com.elenaneacsu.healthmate.utils.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenderBirthFragment extends Fragment {

    private static final String TAG = "mytest";
    private Button mBtnMale;
    private Button mBtnFemale;
    private Button mBtnBirthdate;
    private TextView mTextViewBirthdate;
    private User user;
    public SignUpActivity signUpActivity;

    public static final int REQUEST_CODE = 1;

    public GenderBirthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gender_birth, container, false);

        mBtnMale = view.findViewById(R.id.btn_male);
        mBtnFemale = view.findViewById(R.id.btn_female);
        mBtnBirthdate = view.findViewById(R.id.btn_birthdate);
        mTextViewBirthdate = view.findViewById(R.id.textview_birthdate);
        signUpActivity = (SignUpActivity) getActivity();

        signUpActivity.mCustomViewPager.setEnableSwipe(false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = bundle.getParcelable(USER);
        }

        return view;
    }

    public static GenderBirthFragment newInstance(Bundle arguments) {
        GenderBirthFragment genderBirthFragment = new GenderBirthFragment();
        if (arguments != null) {
            genderBirthFragment.setArguments(arguments);
        }
        return genderBirthFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
                user.setGender(getString(R.string.m));
                setButtonsDesign(mBtnMale, mBtnFemale);
            }
        });

        mBtnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnFemale, mBtnMale);
                user.setGender(getString(R.string.f));
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
            }
        });

        mBtnBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(GenderBirthFragment.this, REQUEST_CODE);
                newFragment.show(getActivity().getSupportFragmentManager(), getString(R.string.select_birthdate));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String selectedDate = data.getStringExtra(SELECTED_DATE);
            DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
            try {
                Date birthdate = dateFormat.parse(selectedDate);
                user.setBirthdate(birthdate);
                Log.d(TAG, "mytest: "+user.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mTextViewBirthdate.setText("Selected date: " + selectedDate);
        }
    }

    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    private void setButtonsDesign(Button buttonOne, Button buttonTwo) {
        buttonOne.setBackgroundResource(R.drawable.button_rounded_color_accent);
        buttonOne.setTextColor(Color.WHITE);
        buttonTwo.setBackgroundResource(R.drawable.button_rounded);
        buttonTwo.setTextColor(getThemePrimaryColor(getContext()));
    }
}
