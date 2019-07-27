package com.softsolstudio.superfarming.utils;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestsent extends Application {
    private static VolleyRequestsent ourInstance;
    private static RequestQueue requestQueue;
    private static final String TAG = "DEFAULT";

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }

    public static VolleyRequestsent getInstance()
    {
        return ourInstance;
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag)? TAG : tag);
        getRequestQueue().add(request);
    }
    public <T> void addRequestQueue(Request<T> request ){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
    public void cancelPendingRequest(Object tag){
        if(requestQueue!=null){
            requestQueue.cancelAll(tag);
        }
    }
}
