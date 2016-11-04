package com.example.androidbtcontrol;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidbtcontrol.adapter.HomeGridAdapter;
import com.example.androidbtcontrol.datamodel.MenuData;

import java.util.ArrayList;

/**
 * Created by Masum on 15/02/2015.
 */
public class MenuFragment extends Fragment {
    private int[] menuIcon = {R.drawable.ico_area, R.drawable.ico_curve, R.drawable.ico_default, R.drawable.ico_columns, R.drawable.ico_l_rd, R.drawable.ico_legend};
    private String[] menuTitle = {"Blood Pressure", "ECG", "SPO2", "Air Flow", "Body Position", "GL Meter"};


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        //setHasOptionsMenu(true);

        Button button = (Button) view.findViewById(R.id.ButtonECG);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openFragment(new ECGFragment());
            }
        });


        RecyclerView recyclerViewHome = (RecyclerView) view.findViewById(R.id.recyclerViewMenu);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_item_space);
        recyclerViewHome.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerViewHome.setHasFixedSize(true);

        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerViewHome.setLayoutManager(manager);

        ArrayList<MenuData> programsDatas = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            MenuData programsData = new MenuData();
            programsData.setMenuTitle("Title " + i);
            programsData.setImage(i);
            //programsDatas.add(programsData);
        }

        for (int i = 0; i < menuIcon.length; i++) {
            MenuData programsData = new MenuData();
            programsData.setMenuTitle(menuTitle[i]);
            programsData.setImage(menuIcon[i]);
            programsDatas.add(programsData);
        }

        final HomeGridAdapter myAdapter = new HomeGridAdapter(programsDatas, getActivity());
        recyclerViewHome.setAdapter(myAdapter);
        myAdapter.openFragment(new HomeGridAdapter.OnOpenFragment() {
            @Override
            public void openFragment(Fragment fragment) {
                ((MainActivity) getActivity()).openFragment(fragment);
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
            Toast.makeText(getActivity(), "Menu Fragment Data has been uploaded", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    public OnChangeCommand onChangeCommand1;

    public void doChange(OnChangeCommand onChangeCommand) {
        onChangeCommand1 = onChangeCommand;

    }

    interface OnChangeCommand {
        void onChangeCommand();
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0)
                outRect.top = space;
        }
    }


}