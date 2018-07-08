package com.example.abhishek.cineverse.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.example.abhishek.cineverse.models.Movie;

/**
 * Movie DAO stores and retrieves {@link Movie} to the database using {@link android.arch.persistence.room.Room}
 */
@Dao
public interface MovieDao {

    /**
     * Insert an single instance of {@link Movie} to the database
     *
     * @param movie The {@link Movie} to insert in database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    /**
     * Insert an Array of {@link Movie} to the database
     *
     * @param movies The {@link Movie} array to insert in database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkMovieInsert(Movie... movies);

}
