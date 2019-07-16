package com.elenaneacsu.healthmate.screens.logging.weight_history;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    OnDataPass onDataPass;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(view.getYear(), view.getMonth(), view.getDayOfMonth());
                    onDataPass.onDatePass(calendar.getTime());
                }
            };

    public interface OnDataPass {
        void onDatePass(Date date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onDataPass = (OnDataPass) context;
    }
}