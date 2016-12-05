package com.example.androidbtcontrol.interfaces;

import android.bluetooth.BluetoothSocket;

/**
 * Created by masum on 10/10/2016.
 */
public interface MainView {
    boolean getConnected(boolean isConnected, BluetoothSocket bluetoothSocket);
    String onDataReceived(String data);
}
