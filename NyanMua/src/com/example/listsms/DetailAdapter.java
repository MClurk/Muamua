package com.example.listsms;

import java.util.List;

import com.example.nyanmua.R;

import android.content.Context;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class DetailAdapter implements ListAdapter {

	private List<DetailEntity> coll;

	private Context ctx;

	DetailEntity entity;

	LinearLayout layout;

	LayoutInflater vi;

	LinearLayout layout_bj;

	TextView tvName;
	TextView tvDate;
	TextView tvText;

	public DetailAdapter(Context context, List<DetailEntity> coll) {
		ctx = context;
		this.coll = coll;
	}

	public boolean areAllItemsEnabled() {
		return true;
	}

	public boolean isEnabled(int arg0) {
		return true;
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		entity = coll.get(position);
		int itemLayout = entity.getLayoutID();

		layout = new LinearLayout(ctx);
		vi = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi.inflate(itemLayout, layout, true);

		layout.setBackgroundColor(0xffB4B4B4);

		layout_bj = (LinearLayout) layout.findViewById(R.id.layout_bj);

		tvText = (TextView) layout.findViewById(R.id.messagedetail_row_text);
		tvText.setText(entity.getText());

		return layout;
	}

	public int getViewTypeCount() {
		return coll.size();
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}
}
