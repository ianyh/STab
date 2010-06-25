package com.scarredions.stab;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
        
        fixAddPersonButtonSize();
        
        // create adapters and link them to each other
        STMenuListAdapter mla = new STMenuListAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        STPersonListAdapter pla = new STPersonListAdapter(this);
        mla.setPersonListAdapter(pla);
        pla.setMenuListAdapter(mla);

        // set up the menu list view
        ListView lv = (ListView) findViewById(R.id.list);
		lv.setAdapter(mla);
		lv.setClickable(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				((CheckedTextView) v).toggle();
			}
		});
		mla.add("test");
		
		// set up the person gallery
		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(mla.getPersonListAdapter());
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			    STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
			    adapter.getMenuListAdapter().notifyDataSetChanged();
			}
		});
    }
    
    public void fixAddPersonButtonSize()
    {
        Button b = (Button) findViewById(R.id.add_person);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) b.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.divider);
        b.setLayoutParams(params);        
    }
    
}
