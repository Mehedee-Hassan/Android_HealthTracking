package com.example.androidbtcontrol.interfaces;

/**
 * Created by apple on 11/7/16.
 */

public interface FragmentView {
    void onReceiveAPIData(Object obj);
    void showMessage();
    void showLoading();
    void hideLoading();
}
