package com.example.xyzreader.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class DatesUtils {

    public static GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

    private static SimpleDateFormat dateFormat() {
        return new SimpleDateFormat(Constants.DATE_SIMPLE_FORMAT, Locale.getDefault());
    }

    public static SimpleDateFormat outputFormat() {
        return new SimpleDateFormat("", Locale.getDefault());
    }

    public static java.util.Date formatDate(String publishedDate) {
        try {
            return dateFormat().parse(publishedDate);
        } catch (ParseException ex) {
            Log.e(TAG, Objects.requireNonNull(ex.getMessage()));
            return new java.util.Date();
        }
    }

}
