package com.example.androidbtcontrol.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidbtcontrol.R;
import com.example.androidbtcontrol.fragments.DeviceListFragment;
import com.example.androidbtcontrol.fragments.MenuFragment;
import com.example.androidbtcontrol.interfaces.MainView;
import com.example.androidbtcontrol.interfaces.OnWriteCompleted;
import com.example.androidbtcontrol.model.BluetoothConnectedThread;
import com.example.androidbtcontrol.presenter.BluetoothConnectionPresenter;
import com.example.androidbtcontrol.utilities.ConstantValues;
import com.example.androidbtcontrol.utilities.HttpRequestQueue;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;

    private TextView textInfo, textStatus;
    private ListView listViewPairedDevice;
    private LinearLayout inputPane;
    private Button btnDisconnect;

    private UUID mUUID;
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



        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "FEATURE_BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //using the well-known SPP UUID
        mUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stInfo = bluetoothAdapter.getName() + "\n" + bluetoothAdapter.getAddress();
        textInfo.setText(stInfo);

//       loadDeviceListFragment();
        createBluetooth();
        loadMenuFragment();
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
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        Log.e("Count", "" + count);

        if (count == 1) {
            getSupportActionBar().setTitle("Health Tracker");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            manager.popBackStack();
            return;
        } else {
            finish();
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            getSupportActionBar().setTitle("Health Tracker");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            invalidateOptionsMenu();
            return true;

        } /*else if (id == R.id.action_settings) {

            if (bluetoothSocket1 != null) {
                try {
                    myThreadConnected.cancel();
                    bluetoothSocket1.close();
                    btConnectionPresenter.closeConnection();

                    Toast.makeText(MainActivity.this, "Connection is turned off.", Toast.LENGTH_LONG).show();
                    textStatus.setText("Disconnected");
                    textStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.amber_dark));
                    loadDeviceListFragment();
                    getSupportActionBar().setTitle("Health Tracker");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Connection", "" + e.getMessage());
                }

            }

            return true;
        }*/

        return super.onOptionsItemSelected(item);
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
                   // btnDisconnect.setVisibility(View.VISIBLE);
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

                            Log.e("DATA", "=>" + strTemp);
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();

                            //Log.d("Data ECG", "" + strTemp);
                            try {
                                float x = Float.parseFloat(strTemp);
                                onReceiveGraphData.onReceiveData(x);
                                //Log.e("DATA", "**" + x);
                            } catch (Exception e) {

                            }
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
                            strTemp = "";
                        }
                        onReceiveData.onReceiveData(strTemp);

                        Log.e("SPO: ", "" + data);
                    } else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_GL_METER)) {
                        strTemp = strTemp.concat(data);
                        if (strTemp.contains("*") && strTemp.contains("#")) {
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();
                            onReceiveData.onReceiveData(strTemp);
                            strTemp = "";
                        }
                        Log.e("GL METER: ", "" + data);
                    } else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_BODY_POSITION)) {
                        strTemp = strTemp.concat(data);
                        if (strTemp.contains("*") && strTemp.contains("#")) {
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();
                            onReceiveData.onReceiveData(strTemp);
                            strTemp = "";
                        }
                        Log.e("BODY POSITION: ", "" + data);
                    } else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_AIR_FLOW)) {
                        strTemp = strTemp.concat(data);
                        if (strTemp.contains("*") && strTemp.contains("#")) {
                            int startOfLineIndex = strTemp.indexOf("*") + 1;
                            int endOfLineIndex = strTemp.indexOf("#");

                            strTemp = strTemp.substring(startOfLineIndex, endOfLineIndex).trim();
                            onReceiveData.onReceiveData(strTemp);
                            strTemp = "";
                        }
                        Log.e("AIR FLOW: ", "" + data);
                    }
                    else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_WEIGHT)) {

                        Log.e("Weight : ", "" + data);
                        onReceiveData.onReceiveData(data);
                    }
                    else if (mSensorType.equalsIgnoreCase(ConstantValues.SENSOR_HEIGHT)) {

                        onReceiveData.onReceiveData(data);
                        Log.e("Height: ", "" + data);
                    }

                    else {
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

        createConnectionThread();



//           Toast.makeText(this,"Not connected yet",Toast.LENGTH_LONG ).show();




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


    public void createConnection(){


        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        boolean flag = false;
        for ( BluetoothDevice bd : pairedDevices){
            Log.d(TAG," device ="+ bd.getName());

            if (bd.getName().equals(ConstantValues.BLUETOOTH_DEVICE_1)){
                communicateToPresenter(bd);


                flag = true;

                Log.d(TAG," connected 1");
            }
        }

        if (flag = false ){
            Toast.makeText(this,
                    "Device could not found name: "
                            +ConstantValues.BLUETOOTH_DEVICE_1
                    ,Toast.LENGTH_LONG ).show();

        }

    }

    public void setup() {
        btConnectionPresenter = new BluetoothConnectionPresenter(this, mUUID);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    createConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

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

        //Fragment fragment = new ECGFragmentDum();;
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

        Log.d(TAG," connected 2");

        textStatus.setText("Connecting...");
        textStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.amber));
        btConnectionPresenter.openConnection(device);


        Log.d(TAG," connected 3");

    }

    public void closeBtConnection() {
        Toast.makeText(MainActivity.this, "Connection is turned off.", Toast.LENGTH_LONG).show();
        if (bluetoothSocket1 != null) {
            try {
                myThreadConnected.cancel();
                bluetoothSocket1.close();
                btConnectionPresenter.closeConnection();
                textStatus.setText("Disconnected");
                textStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.amber_dark));
//                loadDeviceListFragment();
                getSupportActionBar().setTitle("Health Tracker");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Connection", "" + e.getMessage());
            }

        }
    }

    public void openBackButton(String actionTitle) {
        getSupportActionBar().setTitle(actionTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void closeBackButton() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
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


    public void createBluetooth(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiverBluetooth, filter);
    }

    private final BroadcastReceiver mReceiverBluetooth = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                Toast.makeText(MainActivity.this ,"connected ..",Toast.LENGTH_SHORT).show();
//                    }
//                });

                ConstantValues.CONNECTED_TO_DEVICE= true;


            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {

//                Toast.makeText(MainActivity.this ,"disconnected ..",Toast.LENGTH_SHORT).show();
                ConstantValues.CONNECTED_TO_DEVICE= false;

            }
        }
    };

    private void UpdateUi(Intent intent){
    }

    public void createConnectionThread(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    createConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
