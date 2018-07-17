package com.example.abhishek.cineverse.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    @Ignore
    public static final Creator CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @PrimaryKey
    private int id;

    private String title;

    @SerializedName("vote_average")
    private float votes;

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    private String originalTitle;

    private String overview;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdropPath;

    @Expose(serialize = false, deserialize = false)
    private boolean isFavorite;

    @ColumnInfo(name = "date_added")
    @Expose(serialize = false, deserialize = false)
    private Date dateAdded = new Date();

    @ColumnInfo(name = "criteria")
    @Expose(serialize = false, deserialize = false)
    private String criteria;


    /**
     * An instance of Movie to store it's details
     *
     * @param id:            ID of the movie
     * @param title:         Title of the movie
     * @param originalTitle: Title of the movie in native language
     * @param overview:      A synopsis of the movie
     * @param releaseDate:   Release date of the movie
     * @param posterPath:    Relative path for a Poster
     * @param backdropPath:  Relative path for a Backdrop
     * @param votes:         Votes for the movie out of 10
     */
    public Movie(int id, String title, String originalTitle, String overview, String releaseDate, String posterPath, String backdropPath, float votes, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.votes = votes;
        this.isFavorite = isFavorite;
    }

    // Private constructor for Parcelable
    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        votes = in.readFloat();
        isFavorite = in.readByte() != 0;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public float getVotes() {
        return votes;
    }

    public int getId() {
        return id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = !isFavorite;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date date) {
        this.dateAdded = date;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", votes=" + votes +
                ", id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", isFavourite='" + isFavorite + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeFloat(votes);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
