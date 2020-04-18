package example.com.eldareini.eldarmovieapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ApiCall {

    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context context;

    public ApiCall(Context context){
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiCall getInstance(Context context){
        if (mInstance == null){
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public static void make(Context context, String query, Response.Listener<String> listener,
                            Response.ErrorListener errorListener){
        String url = "https://api.themoviedb.org/3/search/multi?api_key=07c664e1eda6cd9d46f1da1dbaefc959&query=" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        ApiCall.getInstance(context).addToRequestQueue(stringRequest);
    }
}
