package com.example.abhishek.cineverse.network;

import android.content.Context;

import com.example.abhishek.cineverse.data.UrlContract;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieService {
    private static final Object LOCK = new Object();
    private static MovieClient sInstance;

    public static MovieClient getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                // 1 MB of cache
                Cache cache = new Cache(context.getCacheDir(), 1024 * 1024);
                OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();
                Retrofit.Builder builder =
                        new Retrofit
                                .Builder()
                                .baseUrl(UrlContract.BASE_API_URL)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create());
                sInstance = builder.build().create(MovieClient.class);
            }
        }
        return sInstance;
    }
}
