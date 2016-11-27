package com.example.androidbtcontrol.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidbtcontrol.AirFlowFragment;
import com.example.androidbtcontrol.BPFragment;
import com.example.androidbtcontrol.BodyPositionFragment;
import com.example.androidbtcontrol.ECGFragment;
import com.example.androidbtcontrol.GLMeterFragment;
import com.example.androidbtcontrol.R;
import com.example.androidbtcontrol.SPO2Fragment;
import com.example.androidbtcontrol.TemperatureFragment;
import com.example.androidbtcontrol.datamodel.MenuData;

import java.util.ArrayList;

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.ViewHolder> {
    private ArrayList<MenuData> mDataset = new ArrayList<>();
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public ImageView imageViewMenuIcon;
        public CardView cardView;

        public ViewHolder(View v, int viewType) {
            super(v);
            //v.setOnClickListener(this);

            textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
            imageViewMenuIcon = (ImageView) v.findViewById(R.id.textView2);
            cardView = (CardView) v.findViewById(R.id.cardView);
        }

        /*@Override
        public void onClick(View v) {
            Fragment fragment = new ECGFragment();;
            onOpenFragment.openFragment(fragment);
        }*/
    }

    public HomeGridAdapter(ArrayList<MenuData> orderDetailsData, Context context) {
        this.mDataset = orderDetailsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_menu, parent, false);
        ;
        ViewHolder vh = new ViewHolder(v, viewType);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textViewTitle.setText(mDataset.get(position).getMenuTitle());
        holder.imageViewMenuIcon.setImageResource(mDataset.get(position).getImage());
        holder.imageViewMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                if (position == 0) {
                    fragment = new BPFragment();
                } else if (position == 1) {
                    fragment = new ECGFragment();
                } else if (position == 2) {
                    fragment = new SPO2Fragment();
                } else if (position == 3) {
                    fragment = new AirFlowFragment();
                } else if (position == 4) {
                    fragment = new BodyPositionFragment();
                } else if (position == 5) {
                    fragment = new GLMeterFragment();
                } else if (position == 6) {
                    fragment = new TemperatureFragment();
                } else {

                }

                onOpenFragment.openFragment(fragment);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public OnOpenFragment onOpenFragment;

    public void openFragment(OnOpenFragment onOpenFragment) {
        this.onOpenFragment = onOpenFragment;
    }

    public interface OnOpenFragment {
        void openFragment(Fragment fragment);
    }


}