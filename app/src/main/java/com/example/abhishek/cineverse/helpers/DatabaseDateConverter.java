package com.example.abhishek.cineverse.helpers;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DatabaseDateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? new Date().getTime() : date.getTime();
    }
}
