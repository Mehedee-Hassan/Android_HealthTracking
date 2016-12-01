package com.example.androidbtcontrol.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.androidbtcontrol.datamodel.HistoryData;
import com.example.androidbtcontrol.interfaces.FragmentView;
import com.example.androidbtcontrol.interfaces.OnResponseComplete;
import com.example.androidbtcontrol.model.ServerApiCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by REFACTOR on 11/4/2016.
 */

public class AllFragmentPresenter {
    Context context;
    ServerApiCallback serverApiCallback;
    FragmentView fragmentView;
    public AllFragmentPresenter(FragmentView fragmentView){
        this.fragmentView = fragmentView;
        serverApiCallback = new ServerApiCallback(context);
    }

    ///sensors/save_data_from_app
    public void postData(String url, Map<String, String> params){
        serverApiCallback.callLoginApi(url, params, new OnResponseComplete() {
            @Override
            public void onRequestComplete(String response) {
                Log.e("response", "msg " + response);
                if (response.equalsIgnoreCase("1")) {
                    fragmentView.onPostCompleted(response);
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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int jsonArraySize = jsonArray.length();
                    ArrayList<HistoryData> historyDatas = new ArrayList<>();
                    for (int i = 0; i < jsonArraySize; i++) {
                        HistoryData historyData = new HistoryData();
                        historyData.setDate(jsonArray.getJSONObject(i).getString("created_at"));
                        historyData.setDatas(jsonArray.getJSONObject(i).getString("datas"));
                        historyDatas.add(historyData);

                    }

                    fragmentView.onReceiveAPIData(historyDatas);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("response", "msg " + response);

            }
        });
    }


}
