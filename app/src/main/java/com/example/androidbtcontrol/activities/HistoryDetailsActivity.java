package com.example.androidbtcontrol.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidbtcontrol.R;

public class HistoryDetailsActivity extends AppCompatActivity {
    private String mDatas = "datas";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        final TextView txtViewValue = (TextView) findViewById(R.id.textView3);
        String data = getIntent().getExtras().getString(mDatas);
        txtViewValue.setText(data);

        Button button = (Button) findViewById(R.id.btnRefresh);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtViewValue.setText("");

            }
        });
    }
}
