package com.example.androidbtcontrol.interfaces;

import android.bluetooth.BluetoothSocket;

/**
 * Created by masum on 10/10/2016.
 */
public interface OnConnected {
    void getConnected(boolean isConnected, BluetoothSocket bluetoothSocket);
}
