package com.scarredions.stab;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;

public class STab extends ListActivity
{
	private String[] items = {"1", "2", "3"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		setListAdapter(new ArrayAdapter(this, R.layout.list_item, items));
		
//		Gallery g = (Gallery) findViewById(R.id.gallery);
//		g.setAdapter(new STPersonAdapter(this));
    }
}
