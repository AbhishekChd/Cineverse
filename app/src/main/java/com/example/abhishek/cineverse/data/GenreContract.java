package com.example.abhishek.cineverse.data;

import android.util.SparseArray;

public final class GenreContract {

    private SparseArray<String> genres = new SparseArray<>();

    private void addGenres() {
        genres.append(28, "Action");
        genres.append(12, "Adventure");
        genres.append(16, "Animation");
        genres.append(35, "Comedy");
        genres.append(80, "Crime");
        genres.append(99, "Documentary");
        genres.append(18, "Drama");
        genres.append(10751, "Family");
        genres.append(14, "Fantasy");
        genres.append(36, "History");
        genres.append(27, "Horror");
        genres.append(10402, "Music");
        genres.append(9648, "Mystery");
        genres.append(10749, "Romance");
        genres.append(878, "Science Fiction");
        genres.append(10770, "TV Movie");
        genres.append(53, "Thriller");
        genres.append(10752, "War");
        genres.append(37, "Western");
    }

    public String getGenre(int id) {
        if (genres.size() <= 0)
            addGenres();
        return genres.get(id, "");
    }
}
