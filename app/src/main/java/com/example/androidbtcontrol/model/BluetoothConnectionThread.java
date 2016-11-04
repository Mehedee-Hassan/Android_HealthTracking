package com.example.androidbtcontrol.model;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.androidbtcontrol.interfaces.OnConnected;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by masum on 10/10/2016.
 */
public class BluetoothConnectionThread implements Runnable{
    private BluetoothSocket bluetoothSocket = null;
    private OnConnected onConnected;

    public BluetoothConnectionThread(BluetoothDevice device, UUID myUUID, OnConnected onConnected) {
        this.onConnected = onConnected;

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
        } catch (IOException e) {
            Log.e("Socket1 Con", "EX::::" + e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            bluetoothSocket.connect();
            onConnected.getConnected(true, bluetoothSocket);
        } catch (IOException e) {
            try {
                bluetoothSocket.close();
            } catch (IOException e1) {
                Log.e("Socket2 Con", "EX::::" + e.getMessage());
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }

}











