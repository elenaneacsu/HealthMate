package com.elenaneacsu.healthmate.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static Date getDateWithNoTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static String stringifiedDate(Date date) {
        //DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        return dateFormat.format(date);
    }
}
