package com.example.androidbtcontrol.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.androidbtcontrol.interfaces.OnResponseComplete;
import com.example.androidbtcontrol.model.ServerApiCallback;

import java.util.Map;

/**
 * Created by REFACTOR on 11/4/2016.
 */

public class AllFragmentPresenter {
    Context context;
    ServerApiCallback serverApiCallback;
    public AllFragmentPresenter(Context context){
        this.context = context;
        serverApiCallback = new ServerApiCallback(context);
    }

    ///sensors/save_data_from_app
    public void postData(String url, Map<String, String> params){
        serverApiCallback.callLoginApi(url, params, new OnResponseComplete() {
            @Override
            public void onRequestComplete(String response) {
                Log.e("response", "msg " + response);
                if (response.equalsIgnoreCase("1")) {
                    Toast.makeText(context, "ECG Data has been uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Could not uploaded!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void getApiData(String url, Map<String, String> params){
        serverApiCallback.callLoginApi(url, params, new OnResponseComplete() {
            @Override
            public void onRequestComplete(String response) {
                Log.e("response", "msg " + response);
                if (response.equalsIgnoreCase("1")) {
                    Toast.makeText(context, "ECG Data has been uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Could not uploaded!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
