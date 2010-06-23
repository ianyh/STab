package com.scarredions.stab;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.scarredions.stab.STPersonListAdapter;

// TODO: menu for adding people
// TODO: menu for adding items
// TODO: bind to contacts for people auto complete/images

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
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, items));
		lv.setClickable(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				((CheckedTextView) v).toggle();
			}
		});
		
		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new STPersonListAdapter(this));
    }
}
