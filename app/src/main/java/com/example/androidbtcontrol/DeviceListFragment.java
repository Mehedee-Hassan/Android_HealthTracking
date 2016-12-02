package com.example.androidbtcontrol;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Masum on 15/02/2015.
 */
public class DeviceListFragment extends Fragment {
    private ArrayList<BluetoothDevice> pairedDeviceArrayList;

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

                ArrayAdapter<BluetoothDevice> pairedDeviceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, pairedDeviceArrayList);
                listView.setAdapter(pairedDeviceAdapter);

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

                        /*textStatus.setText("Connecting...");
                        btConnectionPresenter.openConnection(device);*/
                        //((MainActivity) getActivity()).loadMenuFragment();
<<<<<<< HEAD
<<<<<<< HEAD
                        ((MainActivity) getActivity()).communicateToPresenter(device);
                        openDilaog();
=======

                        // Open to production

                        ((MainActivity) getActivity()).communicateToPresenter(device);
                        //openDilaog();
>>>>>>> 5a62fb2aee6006ea60b69198d206b7b040d5023e
=======

                        ((MainActivity) getActivity()).communicateToPresenter(device);
                        openDilaog();
>>>>>>> 65b9a1a05cfb309a2b9edceae5a18e30ee0ecc04

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