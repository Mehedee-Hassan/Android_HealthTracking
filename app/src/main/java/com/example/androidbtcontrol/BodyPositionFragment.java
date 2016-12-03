package com.example.androidbtcontrol;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidbtcontrol.adapter.HistoryListAdapter;
import com.example.androidbtcontrol.datamodel.HistoryData;
import com.example.androidbtcontrol.interfaces.FragmentView;
import com.example.androidbtcontrol.presenter.AllFragmentPresenter;
import com.example.androidbtcontrol.utilities.ConstantValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Masum on 15/02/2015.
 */
public class BodyPositionFragment extends Fragment implements FragmentView {
    private String mDatas = "datas";
    private String mDate = "date";
    private String mPatientId = "";
    private String mTestId = "";
    private StringBuilder mStringBuilder = new StringBuilder();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_position, container,false);
        setHasOptionsMenu(true);

        final TextView txtViewValue = (TextView) view.findViewById(R.id.textViewValue);

        Button button = (Button) view.findViewById(R.id.btnRefresh);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtViewValue.setText("");
                ((MainActivity)getActivity()).doWrite(ConstantValues.SENSOR_BODY_POSITION, new MainActivity.OnReceiveData() {
                    @Override
                    public void onReceiveData(String data) {
                        txtViewValue.append(data.toString());
                    }
                });
            }
        });

        ((MainActivity)getActivity()).doWrite(ConstantValues.SENSOR_BODY_POSITION, new MainActivity.OnReceiveData() {
            @Override
            public void onReceiveData(String data) {
                txtViewValue.append(data.toString());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_for_upload_data, menu);
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
        } else if (id == R.id.action_upload) {
            Toast.makeText(getActivity(), "Body Position Data has been uploaded", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.action_record) {
            Map<String, String> params = new HashMap<>();
            params.put("client_id", "1");
            new AllFragmentPresenter(this).getApiData("sensors/view_sensors_datas_api/1", params);
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialog(final ArrayList<HistoryData> list) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_history);

        final ListView listView = (ListView) dialog.findViewById(R.id.listHistory);
        //ArrayAdapter<String> pairedDeviceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        //listView.setAdapter(pairedDeviceAdapter);

        HistoryListAdapter arrayAdapter = new HistoryListAdapter(getActivity(), list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HistoryDetailsActivity.class);
                intent.putExtra(mDate,list.get(position).getDate());
                intent.putExtra(mDatas, list.get(position).getDatas());
                startActivity(intent);

            }
        });

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

    private void openDialog(final boolean dialogType) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_entry_patient_info);

        final EditText editTextPatientId = (EditText) dialog.findViewById(R.id.editTextId);
        final EditText editTextTestId = (EditText) dialog.findViewById(R.id.editTextTestId);

        if (dialogType) {
            editTextTestId.setVisibility(View.VISIBLE);
        } else {
            editTextTestId.setVisibility(View.GONE);
        }

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
                mPatientId = editTextPatientId.getText().toString();
                mTestId = editTextTestId.getText().toString();

                if (dialogType) {

                    Map<String, String> params = new HashMap<>();
                    params.put("patient_id", mPatientId);
                    params.put("test_id", mTestId);
                    params.put("data", mStringBuilder.toString());
                    params.put("sensor_type", ConstantValues.SENSOR_ECG);
                    params.put("userid", "1");
                    new AllFragmentPresenter(BodyPositionFragment.this).postData("sensors/save_data_from_app", params);


                } else{
                    Map<String, String> params = new HashMap<>();
                    params.put("patient_id", "1");
                    new AllFragmentPresenter(BodyPositionFragment.this).getApiData("sensors/view_sensors_data_api/"+ mPatientId+"/" + ConstantValues.SENSOR_ECG, params);

                }

                dialog.dismiss();


            }
        });

        dialog.show();
    }

    public OnChangeCommand onChangeCommand1;
    public void doChange(OnChangeCommand onChangeCommand) {
        onChangeCommand1 = onChangeCommand;

    }

    @Override
    public void onReceiveAPIData(Object obj) {
        ArrayList<HistoryData> historyDatas = (ArrayList<HistoryData>) obj;
        ArrayList<String> strings = new ArrayList<>();
        for (HistoryData s: historyDatas) {
            strings.add(s.getDate());

        }
        openDialog(historyDatas);
    }

    @Override
    public void onPostCompleted(Object obj) {

    }

    @Override
    public void showMessage() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    interface OnChangeCommand{
        void onChangeCommand();
    }



}