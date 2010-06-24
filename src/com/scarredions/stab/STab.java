package com.scarredions.stab;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.scarredions.stab.STPersonListAdapter;

// TODO: bind to contacts for people/images
// TODO: bind to contacts for autocomplete

public class STab extends Activity
{	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        STMenuListAdapter mla = new STMenuListAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        STPersonListAdapter pla = new STPersonListAdapter(this);
        mla.setPersonListAdapter(pla);
        pla.setMenuListAdapter(mla);
        
        ListView lv = (ListView) findViewById(R.id.list);
        Button b = new Button(this);
        b.setText("Add Menu Item");
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                return;
            }
        });
        lv.addFooterView(b);
		lv.setAdapter(mla);
		lv.setClickable(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				((CheckedTextView) v).toggle();
			}
		});
		
		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(mla.getPersonListAdapter());
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			    ((STPersonListAdapter) parent.getAdapter()).getMenuListAdapter().notifyDataSetChanged();
			}
		});
    }
}
