package com.example.androidbtcontrol.fragments;

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

import com.example.androidbtcontrol.activities.MainActivity;
import com.example.androidbtcontrol.R;

/**
 * Created by Masum on 15/02/2015.
 */
public class WriteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write, container,false);
        setHasOptionsMenu(true);

        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).doWrite("a");
                //onChangeCommand1.onChangeCommand();
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


}