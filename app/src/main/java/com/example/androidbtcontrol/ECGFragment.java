package com.example.androidbtcontrol;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.androidbtcontrol.adapter.HistoryListAdapter;
import com.example.androidbtcontrol.datamodel.HistoryData;
import com.example.androidbtcontrol.interfaces.FragmentView;
import com.example.androidbtcontrol.presenter.AllFragmentPresenter;
import com.example.androidbtcontrol.utilities.ConstantValues;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Masum on 15/02/2015.
 */
public class ECGFragment extends Fragment implements OnChartValueSelectedListener, FragmentView {
    private static final Random RANDOM = new Random();
    public GraphView graphView;

    GraphView graph;
    Handler handler = new Handler();
    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;

    private String mDatas = "datas";
    private String mDate = "date";
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private LineChart mChart;
    private String mPatientId = "";
    private String mTestId = "";
    private StringBuilder mStringBuilder = new StringBuilder();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ecg, container, false);
        setHasOptionsMenu(true);
        final EditText input = (EditText) v.findViewById(R.id.input);
        final Button send = (Button) v.findViewById(R.id.send);
        mChart = (LineChart) v.findViewById(R.id.chart1);

        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);

        // add an empty data object
        mChart.setData(new LineData());
        mChart.getXAxis().setDrawLabels(false);
        mChart.getXAxis().setDrawGridLines(false);

        mChart.invalidate();

        /* Make dummy data for uploading to the server

        for (int i = 0; i < 100; i++) {
            float x = (float) (Math.random() * 50f) + 50f;
            mStringBuilder.append(x + ",");
        }*/


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).doWrite(input.getText().toString(), new MainActivity.OnReceiveGraphData() {
                    @Override
                    public void onReceiveData(final float data) {
                        Log.e("VAL::: ", "value = " + data);
                        mStringBuilder.append(String.valueOf(data) + ",");

                        final Runnable r = new Runnable() {
                            public void run() {
                                addEntry(data);

                            }
                        };

                        handler.postDelayed(r, 1000);

                    }
                });
            }
        });


        /*((MainActivity) getActivity()).doWrite(ConstantValues.SENSOR_ECG, new MainActivity.OnReceiveGraphData() {
            @Override
            public void onReceiveData(final float data) {
                Log.e("VAL::: ", "value = " + data);

                final Runnable r = new Runnable() {
                    public void run() {
                        addEntry(data);

                    }
                };

                handler.postDelayed(r, 1000);

            }
        });*/

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getFragmentManager().popBackStack();
            return true;

        } else if (id == R.id.action_upload) {
            openDialog(true);


        } else if (id == R.id.action_record) {
            openDialog(false);
       }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_for_upload_data, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

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
        String response = (String) obj;
        if (response.equals("1")) {
            Toast.makeText(getActivity(), "Data has been uploaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        mStringBuilder = new StringBuilder();

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

    private void addEntry(float yData) {

        LineData data = mChart.getData();

        ILineDataSet set = data.getDataSetByIndex(0);
//        set.setDrawValues(false);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        // choose a random dataSet
        int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
        float yValue = (float) (Math.random() * 10) + 50f;

        data.addEntry(new Entry(data.getDataSetByIndex(randomDataSetIndex).getEntryCount(), yData), randomDataSetIndex);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();

        mChart.setVisibleXRangeMaximum(15);
        //mChart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);
        mChart.invalidate();

    }

    private void removeLastEntry() {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set != null) {

                Entry e = set.getEntryForXValue(set.getEntryCount() - 1, Float.NaN);

                data.removeEntry(e, 0);
                // or remove by index
                // mData.removeEntryByXValue(xIndex, dataSetIndex);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }
    }

    private void addDataSet() {

        LineData data = mChart.getData();

        if (data != null) {

            int count = (data.getDataSetCount() + 1);

            ArrayList<Entry> yVals = new ArrayList<Entry>();

            for (int i = 0; i < data.getEntryCount(); i++) {
                yVals.add(new Entry(i, (float) (Math.random() * 50f) + 50f * count));
            }

            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(Color.WHITE);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private void removeDataSet() {

        LineData data = mChart.getData();

        if (data != null) {

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet ECG");
        set.setDrawValues(false);
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }

    private void openDialog(final ArrayList<HistoryData> list) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_history);

        final ListView listView = (ListView) dialog.findViewById(R.id.listHistory);
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
                    new AllFragmentPresenter(ECGFragment.this).postData("sensors/save_data_from_app", params);


                } else{
                    Map<String, String> params = new HashMap<>();
                    params.put("patient_id", "1");
                    new AllFragmentPresenter(ECGFragment.this).getApiData("sensors/view_sensors_data_api/"+ mPatientId+"/" + ConstantValues.SENSOR_ECG, params);

                }

                dialog.dismiss();


            }
        });

        dialog.show();
    }


}