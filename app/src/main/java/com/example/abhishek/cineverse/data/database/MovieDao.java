package com.example.abhishek.cineverse.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.abhishek.cineverse.models.FavouriteMovieEntry;
import com.example.abhishek.cineverse.models.Movie;

import java.util.List;

/**
 * Movie DAO stores and retrieves {@link Movie} to the database using {@link android.arch.persistence.room.Room}
 */
@Dao
public interface MovieDao {

    /**
     * Insert an Array of {@link Movie} to the database
     *
     * @param movies The {@link Movie} array to insert in database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkMovieInsert(Movie... movies);

    /**
     * Return Movies live data based on selected criteria
     *
     * @param criteria Criteria to fetch from API endpoint
     * @return LiveData for the specified criteria
     */
    @Query("SELECT * FROM movie WHERE criteria = :criteria")
    List<Movie> loadByCriteria(String criteria);

    @Insert
    void insertFavouriteMovie(FavouriteMovieEntry favouriteMovieEntry);

    @Query("SELECT movie.* FROM movie, favourites" +
            " WHERE movie.id == favourites.id" +
            " ORDER BY favourites.date_added DESC")
    LiveData<List<Movie>> getAllFavouriteMovies();

    @Query("SELECT COUNT(id) FROM favourites WHERE id = :id")
    int getFavouriteCountById(int id);

    @Query("SELECT COUNT(id) > 0 FROM favourites WHERE id = :id")
    LiveData<Boolean> isFavourite(int id);

    @Query("DELETE FROM favourites WHERE id = :id")
    void deleteFromFavourites(int id);
}
