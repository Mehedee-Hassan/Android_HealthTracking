package com.example.androidbtcontrol.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.androidbtcontrol.interfaces.OnResponseComplete;
import com.example.androidbtcontrol.utilities.ConstantValues;
import com.example.androidbtcontrol.utilities.HttpRequestQueue;

import java.util.Map;

/**
 * Created by masum on 22/08/2015.
 */
public class ServerApiCallback {

    Context context;
    OnResponseComplete apiCallingInterface;

    public ServerApiCallback(Context context) {
        this.context = context;
    }

    public void callLoginApi(String url, final Map<String, String> params, OnResponseComplete loginApiInterface) {
        this.apiCallingInterface = loginApiInterface;
        Log.e("ÜRL", "" + ConstantValues.BASE_API_URL + url);
        StringRequest myReq = new StringRequest(Request.Method.POST, ConstantValues.BASE_API_URL + url, createMyReqSuccessListener(), createMyReqErrorListener()) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Log.e("PARAMS", params.toString());
                return params;
            }
        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);
        HttpRequestQueue.getInstance().addToRequestQueue(myReq);

    }

    /**
     * Response listener for login api
     */
    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    apiCallingInterface.onRequestComplete(response);
                } catch (Exception e2) {

                }
            }

        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    apiCallingInterface.onRequestComplete("Something went wrong");
                } catch (Exception e2) {

                }
            }
        };
    }

}
