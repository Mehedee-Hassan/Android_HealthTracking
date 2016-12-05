package com.example.androidbtcontrol.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidbtcontrol.R;
import com.example.androidbtcontrol.datamodel.HistoryData;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {
    private String  dishName = null;
    private String restaurantId = null;
	private String restauranId = null;
    private ArrayList<BluetoothDevice> optionDataArrayList;
	private Context context;
	private LayoutInflater mInflater;


	public DeviceListAdapter(Context context, ArrayList<BluetoothDevice> optionDatas) {
        this.dishName = dishName;
        this.restauranId = restauranId;
        this.optionDataArrayList = optionDatas;
		this.context = context;

	}

	@Override
	public int getCount() {
		return this.optionDataArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.optionDataArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
            holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_device, null);
			holder.txtViewOptionName = (TextView) convertView.findViewById(R.id.textViewTitle);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtViewOptionName.setText(optionDataArrayList.get(position).getName());


		return convertView;
	}

	//Static dssg
	static class ViewHolder {
		TextView txtViewOptionName;

	}

}
