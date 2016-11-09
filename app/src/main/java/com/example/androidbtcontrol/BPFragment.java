package com.example.androidbtcontrol;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidbtcontrol.adapter.HistoryListAdapter;
import com.example.androidbtcontrol.datamodel.HistoryData;
import com.example.androidbtcontrol.interfaces.FragmentView;
import com.example.androidbtcontrol.presenter.AllFragmentPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Masum on 15/02/2015.
 */
public class BPFragment extends Fragment implements FragmentView {
    private String mDatas = "datas";
    private String mDate = "date";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_bp, container,false);
        setHasOptionsMenu(true);

        final TextView txtViewValue = (TextView) view.findViewById(R.id.textViewValue);

        Button button = (Button) view.findViewById(R.id.btnRefresh);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtViewValue.setText("");
                ((MainActivity)getActivity()).doWrite("b", new MainActivity.OnReceiveData() {
                    @Override
                    public void onReceiveData(String data) {
                        txtViewValue.append(data.toString());
                    }
                });
            }
        });

        ((MainActivity)getActivity()).doWrite("b", new MainActivity.OnReceiveData() {
            @Override
            public void onReceiveData(String data) {
                txtViewValue.append(data.toString());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
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
            Map<String, String> params = new HashMap<>();
            params.put("client_id", "1");
            params.put("datas", "BP Data");
            params.put("sensor_type", "2");
            params.put("userid", "1");
            new AllFragmentPresenter(this).postData("sensors/save_data_from_app", params);

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
                Intent intent = new Intent(getActivity(), DetailsECGActivity.class);
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

    public OnChangeCommand onChangeCommand1;
    public void doChange(OnChangeCommand onChangeCommand) {
        onChangeCommand1 = onChangeCommand;

    }

    @Override
    public void onReceiveAPIData(Object obj) {

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