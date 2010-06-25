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
    
    private STMenuListAdapter menuListAdapter;
    private STPersonListAdapter personListAdapter;
    private Button addPersonButton;
    private Button addMenuItemButton;
    
    /** Called when the activity is first created. */
    @Override 
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addPersonButton = fixAndGetAddPersonButton();
        addPersonButton.setOnClickListener(this);
        
        addMenuItemButton = (Button) findViewById(R.id.add_menu_item_button);
        addMenuItemButton.setOnClickListener(this);
        
        // create adapters and link them to each other
        menuListAdapter = new STMenuListAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        personListAdapter = new STPersonListAdapter(this, R.layout.contact);
        menuListAdapter.setPersonListAdapter(personListAdapter);
        personListAdapter.setMenuListAdapter(menuListAdapter);

        // set up the menu list view
        ListView menuListView = (ListView) findViewById(R.id.menu_list_view);
        menuListView.setAdapter(menuListAdapter);
        menuListView.setClickable(true);
        menuListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ((CheckedTextView) v).toggle();
            }
        });
        
        // set up the person gallery
        Gallery personListView = (Gallery) findViewById(R.id.person_list_view);
        personListView.setAdapter(personListAdapter);
        personListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
                adapter.getMenuListAdapter().notifyDataSetChanged();
            }
        });
        personListAdapter.add("you");
    }
    
    public Button fixAndGetAddPersonButton()
    {
        Button b = (Button) findViewById(R.id.add_person_button);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) b.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.divider);
        b.setLayoutParams(params);        
        return b;
    }
    
    public void onClick(View v)
    {
        Button b = (Button) v;
        if (b == addPersonButton)
            personListAdapter.addPersonByDialog();
        else if (b == addMenuItemButton)
            menuListAdapter.addMenuItemByDialog();
    }
    
}
