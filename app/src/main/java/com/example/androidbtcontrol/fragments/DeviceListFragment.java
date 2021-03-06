package com.example.androidbtcontrol.fragments;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidbtcontrol.activities.MainActivity;
import com.example.androidbtcontrol.R;
import com.example.androidbtcontrol.adapter.DeviceListAdapter;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Masum on 15/02/2015.
 */
public class DeviceListFragment extends Fragment {

    private ArrayList<BluetoothDevice> pairedDeviceArrayList;
    private static final String TAG = "DeviceListFragmentDum";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device_list, container,false);
        final ListView listView = (ListView) v.findViewById(R.id.pairedlist);

        ((MainActivity) getActivity()).getDeviceList(new MainActivity.OnCreatedDeviceList() {
            @Override
            public void getDeviceList(BluetoothAdapter bluetoothAdapter) {

                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();


                if (pairedDevices.size() > 0) {
                    pairedDeviceArrayList = new ArrayList<>();

                    for (BluetoothDevice device : pairedDevices) {
                        pairedDeviceArrayList.add(device);
                    }
                }

                DeviceListAdapter deviceListAdapter = new DeviceListAdapter(getActivity(), pairedDeviceArrayList);
                ///ArrayAdapter<BluetoothDevice> pairedDeviceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, pairedDeviceArrayList);
                listView.setAdapter(deviceListAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BluetoothDevice device = (BluetoothDevice) parent.getItemAtPosition(position);
                        Toast.makeText(getActivity(),
                                "Name: " + device.getName() + "\n"
                                        + "Address: " + device.getAddress() + "\n"
                                        + "BondState: " + device.getBondState() + "\n"
                                        + "BluetoothClass: " + device.getBluetoothClass() + "\n"
                                        + "Class: " + device.getClass(), Toast.LENGTH_LONG).show();


                        //For testing purpose
                        //((MainActivity) getActivity()).loadMenuFragment();

                        //When device available
                        ((MainActivity) getActivity()).communicateToPresenter(device);

                        Log.d(TAG,"communication "+device.getName());
                        //openDilaog();
                    }
                });

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            getFragmentManager().popBackStack();
            //get.setDisplayHomeAsUpEnabled(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public OnChangeCommand onChangeCommand1;
    public void doChange(OnChangeCommand onChangeCommand) {
        onChangeCommand1 = onChangeCommand;

    }

    interface OnChangeCommand{
        void onChangeCommand();
    }

    private void openDilaog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_entry_patient_info);

        final EditText editTextEmail = (EditText) dialog.findViewById(R.id.editTextId);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

}