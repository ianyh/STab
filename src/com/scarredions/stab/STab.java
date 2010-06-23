package com.scarredions.stab;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ListView;

import com.scarredions.stab.STPersonListAdapter;

public class STab extends Activity
{
	static final String[] items = {"1", "2", "3"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ListView lv = (ListView) findViewById(R.id.list);
		
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
		
		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new STPersonListAdapter(this));
    }
}
