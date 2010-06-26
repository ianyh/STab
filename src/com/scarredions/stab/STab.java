package com.scarredions.stab;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Gallery;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.scarredions.stab.STPersonListAdapter;

public class STab extends Activity implements OnClickListener
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
        
        STDataController dataController = new STDataController();
        
        // create adapters
        menuListAdapter = new STMenuListAdapter(this, dataController);
        personListAdapter = new STPersonListAdapter(this, R.layout.contact, dataController);
        personListAdapter.setMenuListAdapter(menuListAdapter);

        // set up the menu list view
        ListView menuListView = (ListView) findViewById(R.id.menu_list_view);
        View menuListTotal = inflateMenuListTotalView();
        menuListView.addFooterView(menuListTotal);
        menuListView.setAdapter(menuListAdapter);
        menuListView.setClickable(true);
        menuListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                STMenuListAdapter mla = (STMenuListAdapter) ((HeaderViewListAdapter) parent.getAdapter()).getWrappedAdapter();
                if (mla.getDataController().getTotalView() == v)
                    return;
                
                LinearLayout layout = (LinearLayout) v;
                CheckedTextView menuItemPrice = (CheckedTextView) layout.getChildAt(1);
                menuItemPrice.toggle();
                
                mla.getDataController().setSelection(position, menuItemPrice.isChecked());
                mla.getDataController().updateTotal();
            }
        });
        
        dataController.setTotalView((LinearLayout) menuListTotal);
        
        // set up the person gallery
        Gallery personListView = (Gallery) findViewById(R.id.person_list_view);
        personListView.setAdapter(personListAdapter);
        personListView.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
                adapter.getDataController().setCurrentPersonId(position);
                adapter.getDataController().updateTotal();
                adapter.getMenuListAdapter().notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
                parent.setSelection(0);
                adapter.getMenuListAdapter().notifyDataSetChanged();
            }
        });
        personListAdapter.add("you");
        personListAdapter.setPersonListView(personListView);
        
        Cursor cursor = getContacts();
        STContactListAdapter contactsAdapter = new STContactListAdapter(this, 
                android.R.layout.simple_dropdown_item_1line, cursor,
                new String[] { ContactsContract.Data.DISPLAY_NAME }, new int[] { android.R.id.text1 });
        personListAdapter.setContactsAutocompleteAdapter(contactsAdapter);
    }
    
    public Button fixAndGetAddPersonButton()
    {
        Button b = (Button) findViewById(R.id.add_person_button);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) b.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.divider);
        b.setLayoutParams(params);        
        return b;
    }
    
    public View inflateMenuListTotalView()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        return factory.inflate(R.layout.list_total, null);
    }
    
    public Cursor getContacts() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        ContentResolver content = getContentResolver();
        return content.query(uri, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME);
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
