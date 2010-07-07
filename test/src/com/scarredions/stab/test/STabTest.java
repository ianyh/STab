package com.scarredions.stab.test;

import com.scarredions.stab.STab;
import com.scarredions.stab.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

public class STabTest extends ActivityInstrumentationTestCase2<STab> {

    private STab activity;    
    
    public STabTest() {
        super("com.scarredions.stab", STab.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        setActivityInitialTouchMode(false);
        
        activity = getActivity();
    }
    
    /**
     * Helper function for clicking a button.
     * 
     * @param button
     */
    private void clickButton(final Button button) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                button.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
    }
    
    /**
     * Helper function for adding a menu item through the appropriate dialog
     * 
     * @param name
     * @param price 
     */
    public void addMenuItem(final String name, final String price) {
        clickButton((Button) activity.findViewById(R.id.add_menu_item_button));
        
        assertEquals(true, activity.getMenuListAdapter().getAddMenuItemDialog() != null);
        
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog addMenuItemDialog = activity.getMenuListAdapter().getAddMenuItemDialog();
                EditText nameEdit = (EditText) addMenuItemDialog.findViewById(R.id.item_name_edit);
                nameEdit.setText(name);
                EditText priceEdit = (EditText) addMenuItemDialog.findViewById(R.id.item_price_edit);
                priceEdit.setText(price);
                Button okay = addMenuItemDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);        
    }
    
    /**
     * Helper function for adding a person through the appropriate dialog.
     * 
     * @param name
     */
    public void addPerson(final String name) {
        clickButton((Button) activity.findViewById(R.id.add_person_button));
        
        assertEquals(true, activity.getPersonListAdapter().getAddPersonDialog() != null);
        
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog addPersonDialog = activity.getPersonListAdapter().getAddPersonDialog();
                EditText nameEdit = (EditText) addPersonDialog.findViewById(R.id.name_edit);
                nameEdit.setText(name);
                Button okay = addPersonDialog.getButton(DialogInterface.BUTTON_POSITIVE);                
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);        
    }
    
    /**
     * Ensure that adding people correctly adds to the data controller.
     */
    public void testAddPerson() {
        addPerson("test");
        
        assertEquals(activity.getDataController().getPersonName(1), "test");
        assertEquals(activity.getDataController().getPersonPhoto(1), null);
    }
    
    /**
     * Ensure that adding a person with an empty name does not add to the data controller.
     */
    public void testAddNamelessPerson() {
        addPerson("");        
        assertEquals(1, activity.getDataController().getPersonCount());
    }    
    
    /**
     * Ensure adding a menu item adds to the data controller.
     */
    public void testAddMenuItem() {
        addMenuItem("test", "1.99");
        
        assertEquals(activity.getDataController().getMenuItemName(0), "test");
        assertEquals(activity.getDataController().getMenuItemPrice(0), Double.valueOf(1.99));
    }
    
    /**
     * Ensure adding a menu item with elements missing does not add to the data controller.
     */
    public void testAddEmptyMenuItem() {        
        addMenuItem("", "1.99");        
        assertEquals(0, activity.getDataController().getMenuItemCount());
        
        addMenuItem("test", "");
        assertEquals(0, activity.getDataController().getMenuItemCount());
        
        addMenuItem("", "");
        assertEquals(0, activity.getDataController().getMenuItemCount());
    }    
    
}
