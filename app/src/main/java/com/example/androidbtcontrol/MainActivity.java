package com.example.androidbtcontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidbtcontrol.interfaces.OnWriteCompleted;
import com.example.androidbtcontrol.model.BluetoothConnectedThread;
import com.example.androidbtcontrol.presenter.BluetoothConnectionPresenter;
import com.example.androidbtcontrol.utilities.ConstantValues;

import java.io.IOException;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements MainView {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;

    private TextView textInfo, textStatus;
    private ListView listViewPairedDevice;
    private LinearLayout inputPane;
    private Button btnDisconnect;

    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";

    private BluetoothConnectionPresenter btConnectionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInfo = (TextView) findViewById(R.id.info);
        textStatus = (TextView) findViewById(R.id.status);
        listViewPairedDevice = (ListView) findViewById(R.id.pairedlist);
        inputPane = (LinearLayout) findViewById(R.id.inputpane);

        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDisconnect.setVisibility(View.GONE);
                if (bluetoothSocket1 != null) {
                    try {
                        myThreadConnected.cancel();
                        bluetoothSocket1.close();
                        btConnectionPresenter.closeConnection();

                        Toast.makeText(MainActivity.this, "Connection is turned off.", Toast.LENGTH_LONG).show();
                        textStatus.setText("Disconnected");
                        textStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.amber_dark));
                        loadDeviceListFragment();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Connection", ""+ e.getMessage());
                    }

                }
            }
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "FEATURE_BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //using the well-known SPP UUID
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stInfo = bluetoothAdapter.getName() + "\n" + bluetoothAdapter.getAddress();
        textInfo.setText(stInfo);

        loadDeviceListFragment();

    }

    @Override
    protected void onStart() {
        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
        super.onStart();

    }


    @Override
    protected void onDestroy() {
        if (bluetoothSocket1 != null) {
            try {
                myThreadConnected.cancel();
                bluetoothSocket1.close();
                btConnectionPresenter.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                setup();
            } else {
                Toast.makeText(this, "BlueTooth NOT enabled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    BluetoothSocket bluetoothSocket1;
    BluetoothConnectedThread myThreadConnected;

    @Override
    public boolean getConnected(boolean isConnected, final BluetoothSocket bluetoothSocket) {

        if (isConnected) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textStatus.setText("Connected");
                    textStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    btnDisconnect.setVisibility(View.VISIBLE);
                    listViewPairedDevice.setVisibility(View.GONE);
                    bluetoothSocket1 = bluetoothSocket;
                    loadMenuFragment();
                }
            });

            myThreadConnected = new BluetoothConnectedThread(bluetoothSocket, new OnWriteCompleted() {
                @Override
                public String onWriteCompleted(String data) {
                    onDataReceived(data);
                    return null;
                }
            });
            myThreadConnected.start();
        }

        return false;
    }

    String strTemp = "";
    @Override
    public String onDataReceived(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_ECG)) {
                        //Log.e("ECG: ", "**" + data);
                        strTemp = strTemp.concat(data);
                        //if (strTemp.contains(":") && strTemp.contains(",")) {
                        if (strTemp.contains("*") && strTemp.contains("#")) {
<<<<<<< HEAD
                            Log.e("DATA", "=>" + strTemp);
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");
=======
                            int endOfLineIndex = strTemp.indexOf(",");
                            int startOfLineIndex = strTemp.indexOf(":") + 1;
>>>>>>> 5a62fb2aee6006ea60b69198d206b7b040d5023e
                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();

                            //Log.d("Data ECG", "" + strTemp);
                            try{
                                float x = Float.parseFloat(strTemp);
                                onReceiveGraphData.onReceiveData(x);
                                //Log.e("DATA", "**" + x);
                            } catch (Exception e) {

                            }

<<<<<<< HEAD
                            strTemp = "";
                        }
                    } else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_SPO)) {
                        strTemp = strTemp.concat(data);
                        //if (strTemp.contains(":") && strTemp.contains(",")) {
                        if (strTemp.contains("*") && strTemp.contains("#")) {

                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();
                            onReceiveData.onReceiveData(strTemp);
=======
>>>>>>> 5a62fb2aee6006ea60b69198d206b7b040d5023e
                            strTemp = "";
                        }

                        Log.e("SPO: ", "" + data) ;
                    } else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_GL_METER)) {
                        strTemp = strTemp.concat(data);
                        if (strTemp.contains("*") && strTemp.contains("#")) {
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();
                            onReceiveData.onReceiveData(strTemp);
                            strTemp = "";
                        }
                        Log.e("GL METER: ", "" + data) ;
                    } else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_BODY_POSITION)) {
                        strTemp = strTemp.concat(data);
                        if (strTemp.contains("*") && strTemp.contains("#")) {
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();
                            onReceiveData.onReceiveData(strTemp);
                            strTemp = "";
                        }
                        Log.e("BODY POSITION: ", "" + data) ;
                    }else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_AIR_FLOW)) {
                        strTemp = strTemp.concat(data);
                        if (strTemp.contains("*") && strTemp.contains("#")) {
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();
                            onReceiveData.onReceiveData(strTemp);
                            strTemp = "";
                        }
                        Log.e("BODY POSITION: ", "" + data) ;
                    } else {
                        onReceiveData.onReceiveData(data);
                    }

                    //textStatus.append(data);
                } catch (Exception e) {
                    //Log.e("EXCP", "**" + e.getMessage());
                }

            }
        });
        return null;
    }

    // Method and Interface declaration
    private void loadDeviceListFragment() {
        Fragment fragment = new DeviceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = getSupportFragmentManager();
        int count = frgManager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            frgManager.popBackStackImmediate();
        }

        FragmentTransaction ft = frgManager.beginTransaction();
        //ft.addToBackStack(null);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    public void loadMenuFragment() {
        Fragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = getSupportFragmentManager();
        int count = frgManager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            frgManager.popBackStackImmediate();
        }

        FragmentTransaction ft = frgManager.beginTransaction();
        //ft.addToBackStack(null);
        ft.replace(R.id.content_frame, fragment, "MENU_FRAGMENT");
        ft.commit();
    }

    private void setup() {
        btConnectionPresenter = new BluetoothConnectionPresenter(this, myUUID);
    }

    public void doWrite(String data) {
        byte[] bytesToSend = data.getBytes();
        myThreadConnected.write(bytesToSend);
    }

    public String mSensorType = "";

    public void doWrite(String sensorType, OnReceiveData onReceiveData) {
        this.onReceiveData = onReceiveData;
        mSensorType = sensorType;
        doWrite(sensorType);
    }

    //Method overloading for graph data
    public void doWrite(String sensorType, OnReceiveGraphData onReceiveData) {
        this.onReceiveGraphData = onReceiveData;
        mSensorType = sensorType;
        doWrite(sensorType);
    }

    public void getDeviceList(OnCreatedDeviceList onCreatedDeviceList) {
        this.onCreatedDeviceList = onCreatedDeviceList;
        this.onCreatedDeviceList.getDeviceList(this.bluetoothAdapter);
    }

    public void openFragment(Fragment fragment) {
        //Fragment fragment = new ECGFragment();;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = getSupportFragmentManager();
        FragmentTransaction ft = frgManager.beginTransaction();
        int count = frgManager.getBackStackEntryCount();

        for (int i = 0; i < count; ++i) {
            frgManager.popBackStackImmediate();
        }

        ft.addToBackStack(null);
        ft.replace(R.id.content_frame, fragment, "FRAGMENT");
        ft.commit();
    }

    public void communicateToPresenter(BluetoothDevice device) {
        textStatus.setText("Connecting...");
        textStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.amber));
        btConnectionPresenter.openConnection(device);

    }

    OnCreatedDeviceList onCreatedDeviceList;

    public interface OnCreatedDeviceList {
        void getDeviceList(BluetoothAdapter bluetoothAdapter);
    }

    public OnReceiveData onReceiveData;

    public interface OnReceiveData {
        void onReceiveData(String data);
    }

    public OnReceiveGraphData onReceiveGraphData;
    public interface OnReceiveGraphData {
        void onReceiveData(float data);
    }


}
