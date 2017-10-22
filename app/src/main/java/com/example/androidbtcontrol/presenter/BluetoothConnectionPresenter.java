package com.example.androidbtcontrol.presenter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.androidbtcontrol.interfaces.MainView;
import com.example.androidbtcontrol.interfaces.OnConnected;
import com.example.androidbtcontrol.model.BluetoothConnectedThread;
import com.example.androidbtcontrol.model.BluetoothConnectionThread;
import com.example.androidbtcontrol.utilities.ConstantValues;

import java.util.UUID;

/**
 * Created by masum on 10/10/2016.
 */
public class BluetoothConnectionPresenter {
    UUID uuid;
    public MainView mainView;
    public BluetoothSocket bluetoothSocket1;
    BluetoothConnectedThread bluetoothConnectedThread;

    public BluetoothConnectionPresenter(MainView mainView, UUID uuid) {
        this.uuid = uuid;
        this.mainView = mainView;

    }

    public void openConnection(BluetoothDevice bluetoothDevice) {

        BluetoothConnectionThread connection = new BluetoothConnectionThread(bluetoothDevice, uuid, new OnConnected() {
            @Override
            public void getConnected(boolean isConnected, BluetoothSocket bluetoothSocket) {
                mainView.getConnected(isConnected, bluetoothSocket);
                bluetoothSocket1 = bluetoothSocket;

               /* bluetoothConnectedThread = new BluetoothConnectedThread(bluetoothSocket1, new OnWriteCompleted() {
                    @Override
                    public String onWriteCompleted(String data) {
                        mainView.onDataReceived(data);
                        return null;
                    }
                });
                bluetoothConnectedThread.start();*/
            }
        });

        new Thread(connection).start();
    }

    public void writeData(byte[] buffer) {
        if (bluetoothConnectedThread != null) {
            bluetoothConnectedThread.write(buffer);
        } else {
            Log.e("Error Null", "" + bluetoothConnectedThread);
        }

    }

    public void closeConnection() {
        try {
            bluetoothConnectedThread.cancel();
            bluetoothSocket1.close();

        } catch (Exception e) {

        }
    }

    public BluetoothConnectedThread bluetoothCommunicationThread() {
        return this.bluetoothConnectedThread;
    }

}
