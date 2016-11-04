package com.example.androidbtcontrol.model;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.androidbtcontrol.interfaces.OnWriteCompleted;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by masum on 10/10/2016.
 */
public class BluetoothConnectedThread extends Thread {
    private final BluetoothSocket connectedBluetoothSocket;
    public final InputStream connectedInputStream;
    private final OutputStream connectedOutputStream;
    private OnWriteCompleted onWriteCompleted;

    public BluetoothConnectedThread(BluetoothSocket socket, OnWriteCompleted onWriteCompleted) {
        this.onWriteCompleted = onWriteCompleted;
        connectedBluetoothSocket = socket;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("Writing Con*", "EX::::" + e.getMessage());
        }

        connectedInputStream = in;
        connectedOutputStream = out;
    }

    int readBufferPosition;
    int counter;

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            readBufferPosition = 0;
            int bytes = 0;
            //bytes = connectedInputStream.read(buffer);
            while (true) {

                if (connectedInputStream.available() > 0) {
                    bytes = connectedInputStream.read(buffer);
                    String strReceived = new String(buffer, 0, bytes);
                    onWriteCompleted.onWriteCompleted(strReceived);
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Writing Con1", "EX::::" + e.getMessage());
            Thread.currentThread().interrupt();
            final String msgConnectionLost = "Connection lost:\n" + e.getMessage();
            return;
        }

    }

    public void write(byte[] buffer) {
        try {
            connectedOutputStream.write(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("Write2 Con", "EX::::" + e.getMessage());
        }
    }

    public void cancel() {
        try {
            connectedBluetoothSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("Write3 Close", "EX::::" + e.getMessage());
            e.printStackTrace();
        }
    }
}

