package com.example.abhishek.cineverse.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class DateUtils {

    // Non instantiable class
    private DateUtils(){}

    public static String getFullDateFromShortDate(String shortDate){

        SimpleDateFormat givenDateFormat =
                new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat returnDateFormat =
                new SimpleDateFormat("MMM dd, yyyy");

        try {
            return returnDateFormat.format(givenDateFormat.parse(shortDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
