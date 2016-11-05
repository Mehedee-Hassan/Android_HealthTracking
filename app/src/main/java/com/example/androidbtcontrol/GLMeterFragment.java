package com.example.androidbtcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidbtcontrol.presenter.AllFragmentPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Masum on 15/02/2015.
 */
public class GLMeterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gl_meter, container,false);
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
            Toast.makeText(getActivity(), "GL Meter Data has been uploaded", Toast.LENGTH_SHORT).show();
            Map<String, String> params = new HashMap<>();
            params.put("client_id", "1");
            params.put("datas", "GL meter Data");
            params.put("sensor_type", "2");
            params.put("userid", "1");
            new AllFragmentPresenter(getActivity()).getApiData("sensors/save_data_from_app", params);
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



}