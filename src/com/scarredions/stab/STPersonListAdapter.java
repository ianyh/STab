package com.scarredions.stab;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class STPersonListAdapter extends BaseAdapter {

	//private final String[] items = {"1", "2", "3"};
    private ArrayList<String> people;
	private Context mContext;
	private STMenuListAdapter mla;
	private Button addButton;
	
	public STPersonListAdapter(Context c) {
		mContext = c;
		addButton = new Button(c);
		addButton.setText("Add Person");
		// TODO: add click listener to button to open dialog for adding person
		
		people = new ArrayList<String>();
	}
	
	public int getCount() {
	    // number of people plus add button
	    return people.size() + 1;
	}

	public Object getItem(int position) {
	    if (position < people.size())
	        return people.get(position);
	    else
	        return null;
	}

	public long getItemId(int position) {
	    return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	    if (position == people.size()) {
	        return addButton;
	    } else if (convertView == null) {
	        TextView tv = new TextView(mContext);
            tv.setText(people.get(position));
            tv.setHeight(100);    
            tv.setWidth(100);
            tv.setGravity(android.view.Gravity.CENTER);
            return tv;
	    } else {
	        return (TextView) convertView;
	    }
	}
	
	public void setMenuListAdapter(STMenuListAdapter mla) {
	    this.mla = mla;
	}
	
	public STMenuListAdapter getMenuListAdapter() {
	    return mla;
	}
	
	public boolean currentPersonHasSelected(int listPosition) {
	    return false;
	}

}
