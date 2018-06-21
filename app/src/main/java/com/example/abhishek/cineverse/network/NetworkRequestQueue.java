package com.example.abhishek.cineverse.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class will be used often so it uses a singleton method access network requests
 * It will last till lifetime of the app
 */
public class NetworkRequestQueue {

    private static NetworkRequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private NetworkRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkRequestQueue(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
