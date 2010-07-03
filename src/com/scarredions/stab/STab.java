package com.scarredions.stab;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.scarredions.stab.STPersonListAdapter;

public class STab extends Activity implements OnClickListener
{
    private final STContactAccessor contactsAccessor = STContactAccessor.getInstance();
    
    private STMenuListAdapter menuListAdapter;
    private STPersonListAdapter personListAdapter;
    private STDataController dataController;
    private Button addPersonButton;
    private Button addMenuItemButton;
    
    /** Called when the activity is first created. */
    @Override 
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dataController = new STDataController(this, savedInstanceState);
        
        updateLayout();
    }
    
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        dataController.saveInstanceState(bundle);
    }
    
    public void updateLayout() {
        addPersonButton = fixAndGetAddPersonButton();
        addPersonButton.setOnClickListener(this);
        
        addMenuItemButton = (Button) findViewById(R.id.add_menu_item_button);
        addMenuItemButton.setOnClickListener(this);  
        
        // create adapters
        menuListAdapter = new STMenuListAdapter(this, dataController);
        personListAdapter = new STPersonListAdapter(this, dataController);
        personListAdapter.setMenuListAdapter(menuListAdapter);

        View menuListTotal = inflateMenuListTotalView();
        View menuListTax = inflateMenuListTaxView();
        View menuListTip = inflateMenuListTipView();
        LinearLayout footerLayout = new LinearLayout(this);
        footerLayout.setOrientation(LinearLayout.VERTICAL);
        footerLayout.addView(menuListTax);
        footerLayout.addView(menuListTip);
        footerLayout.addView(menuListTotal);
        
        // set up the menu list view
        ListView menuListView = (ListView) findViewById(R.id.menu_list_view);
        menuListView.addFooterView(footerLayout);
        menuListView.setAdapter(menuListAdapter);
        menuListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                STMenuListAdapter mla = (STMenuListAdapter) ((HeaderViewListAdapter) parent.getAdapter()).getWrappedAdapter();
                if (mla.getDataController().getMenuListFooter() == v)
                    return;
                
                LinearLayout layout = (LinearLayout) v;
                CheckedTextView menuItemPrice = (CheckedTextView) layout.findViewById(R.id.list_item_price);
                menuItemPrice.toggle();
                
                mla.getDataController().setSelection(position, menuItemPrice.isChecked());
                mla.getDataController().updateMenuListFooter();
            }
        });
        
        dataController.setMenuListFooter(footerLayout);
        
        // set up the person gallery
        Gallery personListView = (Gallery) findViewById(R.id.person_list_view);
        personListView.setAdapter(personListAdapter);
        personListView.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
                adapter.getDataController().setCurrentPersonId(position);
                adapter.getMenuListAdapter().notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
                parent.setSelection(0);
                adapter.getMenuListAdapter().notifyDataSetChanged();
            }
        });
        personListAdapter.setPersonListView(personListView);
        
        Cursor cursor = getContacts();
        STContactListAdapter contactsAdapter = new STContactListAdapter(this, 
                R.layout.autocomplete_list_item, cursor,
                new String[] { contactsAccessor.getDisplayNameColumnName() },
                new int[] { R.id.autocomplete_text },
                dataController);
        personListAdapter.setContactsAutoCompleteAdapter(contactsAdapter);        
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, STConstants.MENU_EDIT_TAX, 0, "Edit " + STConstants.TAX);
        menu.add(0, STConstants.MENU_EDIT_TIP, 0, "Edit " + STConstants.TIP);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case STConstants.MENU_EDIT_TAX:
            dataController.editTaxByDialog();
            return true;
        case STConstants.MENU_EDIT_TIP:
            dataController.editTipByDialog();
            return true;
        }

        return false;
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
    
    public View inflateMenuListTaxView() {
        LayoutInflater factory = LayoutInflater.from(this);
        LinearLayout taxView = (LinearLayout) factory.inflate(R.layout.list_total, null);
        String taxText = STConstants.TAX + " (" + dataController.getFormattedTaxPercentage() + ")";
        ((TextView) taxView.findViewById(R.id.list_footer_text)).setText(taxText);
        
        return taxView;
    }
    
    public View inflateMenuListTipView() {
        LayoutInflater factory = LayoutInflater.from(this);
        LinearLayout tipView = (LinearLayout) factory.inflate(R.layout.list_total, null);
        String tipText = STConstants.TIP + " (" + dataController.getFormattedTipPercentage() + ")";
        ((TextView) tipView.findViewById(R.id.list_footer_text)).setText(tipText);
        
        return tipView;
    }
    
    public Cursor getContacts() {
        return contactsAccessor.managedQuery(this);
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
