package com.example.abhishek.cineverse.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.abhishek.cineverse.helpers.DatabaseDateConverter;
import com.example.abhishek.cineverse.models.Movie;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
@TypeConverters(DatabaseDateConverter.class)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "cineverse";

    private static final Object LOCK = new Object();
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(
                        context,
                        MovieDatabase.class,
                        DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    // Associated Movie DOA for database
    public abstract MovieDao movieDao();
}
