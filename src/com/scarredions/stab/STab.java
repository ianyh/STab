package com.scarredions.stab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * The main application activity. 
 * Also acts as OnClickListener for buttons for adding people and menu items.
 * 
 * @author ianyh
 *
 */
public class STab extends Activity implements OnClickListener, DialogInterface.OnClickListener
{
    private final STContactAccessor contactsAccessor = STContactAccessor.getInstance();
    
    private STMenuListAdapter menuListAdapter;
    private STPersonListAdapter personListAdapter;
    private STDataController dataController;
    private Button addPersonButton;
    private Button addMenuItemButton;
    
    private AlertDialog editTaxOrTipDialog;
    
    /**
     * Launches a dialog for editing either tax or tip.
     * @param type Type of dialog to launch (tax or tip)
     * @param value The current value of the type.
     */
    private void editByDialog(String type, Double value) {
        LayoutInflater factory = LayoutInflater.from(this);
        LinearLayout dialogLayout = (LinearLayout) factory.inflate(R.layout.dialog_tax_or_tip_entry, null);
        ((TextView) dialogLayout.findViewById(R.id.value_view)).setText(type);
        ((EditText) dialogLayout.findViewById(R.id.value_edit)).setText(value.toString());
        
        editTaxOrTipDialog = new AlertDialog.Builder(this)
            .setView(dialogLayout)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create();
        
        editTaxOrTipDialog.show();
    }
    
    /**
     * Launches a dialog for editing tax.
     */
    public void editTaxByDialog() {
        editByDialog(STConstants.TAX, Double.valueOf(dataController.getTaxPercentage()));
    }
    
    /**
     * Launches a dialog for editing tip.
     */
    public void editTipByDialog() {
        editByDialog(STConstants.TIP, Double.valueOf(dataController.getTipPercentage()));
    }
    
    /**
     * Does some specific processing to make the button layout correctly
     * @return the add person button
     */
    public Button fixAndGetAddPersonButton() {
        Button b = (Button) findViewById(R.id.add_person_button);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) b.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.divider);
        b.setLayoutParams(params);
        return b;
    }
    
    public Cursor getContacts() {
        return contactsAccessor.managedQuery(this);
    }
    
    /**
     * 
     * @return The activity's data controller.
     */
    public STDataController getDataController() {
        return dataController;
    }
    
    /**
     * 
     * @return the AlertDialog for tax/tip editing, or null if there is no dialog open
     */
    public AlertDialog getEditTaxOrTipDialog() {
        return editTaxOrTipDialog;
    }
    
    /**
     * 
     * @return The activity's menu item list's adapter
     */
    public STMenuListAdapter getMenuListAdapter() {
        return menuListAdapter;
    }
    
    /**
     * 
     * @return The activity's person list's adapter
     */
    public STPersonListAdapter getPersonListAdapter() {
        return personListAdapter;
    }
    
    /**
     * Inflates the view for presenting tax in the menu item list footer.
     * @return the inflated view
     */
    public View inflateMenuListTaxView() {
        LayoutInflater factory = LayoutInflater.from(this);
        LinearLayout taxView = (LinearLayout) factory.inflate(R.layout.list_total, null);
        String taxText = STConstants.TAX + " (" + dataController.getFormattedTaxPercentage() + ")";
        ((TextView) taxView.findViewById(R.id.list_footer_text)).setText(taxText);
        
        return taxView;
    }
    
    /**
     * Inflates the view for presenting tip in the menu item list footer
     * @return the inflated view
     */
    public View inflateMenuListTipView() {
        LayoutInflater factory = LayoutInflater.from(this);
        LinearLayout tipView = (LinearLayout) factory.inflate(R.layout.list_total, null);
        String tipText = STConstants.TIP + " (" + dataController.getFormattedTipPercentage() + ")";
        ((TextView) tipView.findViewById(R.id.list_footer_text)).setText(tipText);
        
        return tipView;
    }
    
    /**
     * Inflates the view for presenting totals in the menu item list footer
     * @return the inflated view
     */
    public View inflateMenuListTotalView() {
        LayoutInflater factory = LayoutInflater.from(this);
        LinearLayout totalView = (LinearLayout) factory.inflate(R.layout.list_total, null);
        ((TextView) totalView.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        ((TextView) totalView.getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        return totalView;
    }
    
    public void notifyDataSetChanged() {
        menuListAdapter.notifyDataSetChanged();
        personListAdapter.notifyDataSetChanged();
    }
    
    /**
     * Click handler for tax/tip edit dialog.
     * 
     * Fails silently if the value entered is empty or invalid.
     */
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            String type = ((TextView) d.findViewById(R.id.value_view)).getText().toString();
            String value = ((EditText) d.findViewById(R.id.value_edit)).getText().toString();
            if (value.equals("")) {
                return;
            }
            
            double doubleValue = Double.valueOf(value);
            if (doubleValue < 0 || doubleValue > 1.0) {
                return;
            }
            
            if (type.equals("Tax")) {
                menuListAdapter.setTaxPercentage(Double.valueOf(value));
            } else if (type.equals("Tip")) {
                menuListAdapter.setTipPercentage(Double.valueOf(value));
            }            
        }
        
        editTaxOrTipDialog = null;
        
    }
    
    /**
     * Click listener for add person/menu item buttons.
     */
    public void onClick(View v) {
        Button b = (Button) v;
        if (b == addPersonButton) {
            personListAdapter.addPersonByDialog();
        } else if (b == addMenuItemButton) {
            menuListAdapter.addMenuItemByDialog();
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dataController = new STDataController(this, savedInstanceState);
        
        updateLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, STConstants.MENU_EDIT_TAX, 0, "Edit " + STConstants.TAX);
        menu.add(0, STConstants.MENU_EDIT_TIP, 0, "Edit " + STConstants.TIP);
        menu.add(0, STConstants.MENU_CLEAR_MENU, 0, "Clear Menu");
        menu.add(0, STConstants.MENU_CLEAR_PEOPLE, 0, "Clear People");
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case STConstants.MENU_EDIT_TAX:
            this.editTaxByDialog();
            return true;
        case STConstants.MENU_EDIT_TIP:
            this.editTipByDialog();
            return true;
        case STConstants.MENU_CLEAR_MENU:
            dataController.clearMenuItems();
            menuListAdapter.notifyDataSetChanged();
            return true;
        case STConstants.MENU_CLEAR_PEOPLE:
            dataController.clearPeople();
            personListAdapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }
    
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        dataController.saveInstanceState(bundle);
    }
    
    /**
     * Initializes the main layout.
     */
    public void updateLayout() {
        addPersonButton = fixAndGetAddPersonButton();
        addPersonButton.setOnClickListener(this);
        
        addMenuItemButton = (Button) findViewById(R.id.add_menu_item_button);
        addMenuItemButton.setOnClickListener(this);
        
        // create adapters
        menuListAdapter = new STMenuListAdapter(this, dataController);
        personListAdapter = new STPersonListAdapter(this, dataController);

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
                if (mla.getMenuListFooter() == v) {
                    return;
                }
                
                LinearLayout layout = (LinearLayout) v;
                CheckedTextView menuItemPrice = (CheckedTextView) layout.findViewById(R.id.list_item_price);
                menuItemPrice.toggle();
                
                mla.getDataController().setSelection(position, menuItemPrice.isChecked());
                mla.updateMenuListFooter();
            }
        });
        
        menuListAdapter.setMenuListFooter(footerLayout);
        
        final STab activity = this;
        
        // set up the person gallery
        Gallery personListView = (Gallery) findViewById(R.id.person_list_view);
        personListView.setAdapter(personListAdapter);
        personListView.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                STPersonListAdapter adapter = (STPersonListAdapter) parent.getAdapter();
                adapter.getDataController().setCurrentPersonId(position);
                activity.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
                activity.notifyDataSetChanged();
            }
        });
        
        Cursor cursor = contactsAccessor.managedQuery(this);
        STContactListAdapter contactsAdapter = new STContactListAdapter(this,
                R.layout.autocomplete_list_item, cursor,
                new String[] { contactsAccessor.getDisplayNameColumnName() },
                new int[] { R.id.autocomplete_text },
                dataController);
        personListAdapter.setContactsAutoCompleteAdapter(contactsAdapter);
    }
    
    
}
