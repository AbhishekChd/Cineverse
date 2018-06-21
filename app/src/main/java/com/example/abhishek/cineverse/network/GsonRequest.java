package com.example.abhishek.cineverse.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Gson mGson;
    private final Class<T> mTargetClass;
    private final Map<String, String> headers;
    private final Response.Listener<T> mListener;

    /**
     * A Gson Class to parse generic response
     *
     * @param url           URL of the endpoint
     * @param targetClass   Target Class to be mapped
     * @param headers       Headers for the request
     * @param listener      Listener for response
     * @param errorListener Error Listener to deliver error
     */
    public GsonRequest(String url, Class<T> targetClass, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mTargetClass = targetClass;
        this.headers = headers;
        mListener = listener;
        mGson = new GsonBuilder().create();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mTargetClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        // Just to give back results to original listener
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // To return headers if any
        return headers != null ? headers : super.getHeaders();
    }
}
