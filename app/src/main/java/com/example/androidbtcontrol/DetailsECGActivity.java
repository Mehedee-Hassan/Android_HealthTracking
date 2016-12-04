package com.example.androidbtcontrol;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DetailsECGActivity extends AppCompatActivity {
    public GraphView graphView;
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    GraphView graph;
    private LineChart mChart;
    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
    private String mDatas = "datas";
    private String mDate = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_ecg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mChart = (LineChart) findViewById(R.id.chart1);

        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);

        // add an empty data object
        mChart.setData(new LineData());
        mChart.getXAxis().setDrawLabels(false);
        mChart.getXAxis().setDrawGridLines(false);

        mChart.setMaxVisibleValueCount(30);

        mChart.invalidate();
        //addEntry(0.0f);
        String datas = getIntent().getExtras().getString(mDatas);
        List<String> list = new ArrayList<>(Arrays.asList(datas.split(",")));

        /*for (int i = 0; i < 100; i++) {
            float yValue = (float) (Math.random() * 10) + 50f;
            addEntry(yValue);
        }*/

        for (int i = 0; i < list.size(); i++) {
            float yValue = Float.parseFloat(list.get(i));
            addEntry(yValue);
        }


        //addDataSet();
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet ECG");

        set.setDrawValues(false);
        set.setLineWidth(2.5f);
        //set.setCircleRadius(0.0f);
        set.setDrawCircles(false);
        set.setCircleHoleRadius(2.5f);
        //set.setCircleColor(Color.RED);
        set.setColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(5f);

        set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                ? LineDataSet.Mode.LINEAR
                :  LineDataSet.Mode.CUBIC_BEZIER);
        //set.setDrawCubic(true);
        set.setCubicIntensity(0.01f);

        return set;
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
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

}
