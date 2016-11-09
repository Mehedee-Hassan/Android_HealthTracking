package com.example.androidbtcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HistoryDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        final TextView txtViewValue = (TextView) findViewById(R.id.textViewValue);

        /*String datas = getIntent().getExtras().getString(mDatas);
        List<String> list = new ArrayList<String>(Arrays.asList(datas.split(",")));
        list.get(1);*/

        Button button = (Button) findViewById(R.id.btnRefresh);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtViewValue.setText("");

            }
        });
    }
}
