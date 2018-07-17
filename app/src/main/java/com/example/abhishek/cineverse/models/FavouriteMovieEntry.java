package com.example.abhishek.cineverse.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "favourites")
public class FavouriteMovieEntry {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "date_added")
    private Date dateAdded;

    public FavouriteMovieEntry(int id) {
        this.id = id;
        dateAdded = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
