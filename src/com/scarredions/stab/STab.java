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
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.scarredions.stab.STPersonListAdapter;

// TODO: bind to contacts for people/images
// TODO: bind to contacts for autocomplete

public class STab extends Activity
	implements OnClickListener
{	
	
	private STMenuListAdapter menuList;
	private STPersonListAdapter personList;
	private Button addPerson;
	private Button addMenuItem;
	
    /** Called when the activity is first created. */
    @Override 
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addPerson = fixAndGetAddPersonButton();
        addPerson.setOnClickListener(this);
        
        addMenuItem = (Button) findViewById(R.id.add_item);
        addMenuItem.setOnClickListener(this);
        
        // create adapters and link them to each other
        menuList = new STMenuListAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        personList = new STPersonListAdapter(this);
        menuList.setPersonListAdapter(personList);
        personList.setMenuListAdapter(menuList);

        // set up the menu list view
        ListView lv = (ListView) findViewById(R.id.list);
		lv.setAdapter(menuList);
		lv.setClickable(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				((CheckedTextView) v).toggle();
			}
		});
		menuList.add("test");
		
		// set up the person gallery
		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(menuList.getPersonListAdapter());
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			    STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
			    adapter.getMenuListAdapter().notifyDataSetChanged();
			}
		});
    }
    
    public Button fixAndGetAddPersonButton()
    {
        Button b = (Button) findViewById(R.id.add_person);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) b.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.divider);
        b.setLayoutParams(params);        
        return b;
    }
    
    public void onClick(View v)
    {
    	Button b = (Button) v;
    	if (b == addPerson)
    		personList.addPersonByDialog();
    	
    }
    
}
