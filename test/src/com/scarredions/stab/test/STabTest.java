package com.scarredions.stab.test;

import com.scarredions.stab.STab;
import com.scarredions.stab.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;

public class STabTest extends ActivityInstrumentationTestCase2<STab> {

    private STab activity;    
    
    public STabTest() {
        super("com.scarredions.stab", STab.class);
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
     * 
     * @param position
     * @return true if the menu item at position is selected, false otherwise
     */
    public boolean isMenuItemSelected(int position) {
        ListView menuItemList = (ListView) activity.findViewById(R.id.menu_list_view);
        LinearLayout menuItem = (LinearLayout) menuItemList.getChildAt(position);
        return ((CheckedTextView) menuItem.findViewById(R.id.list_item_price)).isChecked();
    }
    
    /**
     * Selects the person at position in the person list gallery
     * @param position
     */
    public void selectPerson(final int position) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Gallery personList = (Gallery) activity.findViewById(R.id.person_list_view);
                personList.setSelection(position);                
            }
        });
        getInstrumentation().waitForIdleSync();
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        setActivityInitialTouchMode(false);
        
        activity = getActivity();
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
    
    /**
     * Ensure adding a menu item with negative price gets ignored
     */
    public void testAddNegativeMenuItem() {
        addMenuItem("test", "-1.99");
        assertEquals(0, activity.getDataController().getMenuItemCount());
    }
    
    /**
     * Ensure adding a menu item adds to the data controller.
     */
    public void testAddMenuItem() {
        addMenuItem("test", "1.99");
        
        assertEquals("test", activity.getDataController().getMenuItemName(0));
        assertEquals("$1.99", activity.getDataController().getMenuItemPrice(0));
    }
    
    /**
     * Ensure that adding a person with an empty name does not add to the data controller.
     */
    public void testAddNamelessPerson() {
        addPerson("");        
        assertEquals(1, activity.getDataController().getPersonCount());
    }
    
    /**
     * Ensure that adding people correctly adds to the data controller.
     */
    public void testAddPerson() {
        addPerson("test");
        
        assertEquals("test", activity.getDataController().getPersonName(1));
        assertEquals(null, activity.getDataController().getPersonPhoto(1));
    }
    
    /**
     * Ensure that switching between people preserves selection state.
     */
    public void testSwitchSelections() {
        addPerson("test");
        addMenuItem("test1", "1.00");
        
        selectPerson(1);
        toggleMenuItemSelected(0);        
        assertEquals(true, isMenuItemSelected(0));
        
        selectPerson(0);
        assertEquals(false, isMenuItemSelected(0));
    }
    
    /**
     * Toggles the checked state of the menu item at position
     * @param position
     */
    public void toggleMenuItemSelected(final int position) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                ListView menuItemList = (ListView) activity.findViewById(R.id.menu_list_view);
                LinearLayout menuItem = (LinearLayout) menuItemList.getChildAt(position);
                ((CheckedTextView) menuItem.findViewById(R.id.list_item_price)).toggle();                
            }
        });
        getInstrumentation().waitForIdleSync();
    }
    
//    
//    public void testClearMenuItems() {
//        // TODO: implement
//    }
//    
//    public void testClearPeople() {
//        // TODO: implement
//    }
//    
    public void testEditTax() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.editTaxByDialog();
                AlertDialog dialog = activity.getEditTaxOrTipDialog();
                EditText valueEdit = (EditText) dialog.findViewById(R.id.value_edit);
                valueEdit.setText("0.18");
                Button okay = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        getInstrumentation().waitForIdleSync();
        
        assertEquals(0.18, activity.getDataController().getTaxPercentage());
    }
    
    public void testEditTaxInvalid() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.editTaxByDialog();
                AlertDialog dialog = activity.getEditTaxOrTipDialog();
                EditText valueEdit = (EditText) dialog.findViewById(R.id.value_edit);
                valueEdit.setText("-0.18");
                Button okay = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        getInstrumentation().waitForIdleSync();
        
        assertEquals(0.08, activity.getDataController().getTaxPercentage());
        
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.editTaxByDialog();
                AlertDialog dialog = activity.getEditTaxOrTipDialog();
                EditText valueEdit = (EditText) dialog.findViewById(R.id.value_edit);
                valueEdit.setText("1.18");
                Button okay = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        getInstrumentation().waitForIdleSync();
        
        assertEquals(0.08, activity.getDataController().getTaxPercentage());        
    }
    
    public void testEditTip() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.editTipByDialog();
                AlertDialog dialog = activity.getEditTaxOrTipDialog();
                EditText valueEdit = (EditText) dialog.findViewById(R.id.value_edit);
                valueEdit.setText("0.18");
                Button okay = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        getInstrumentation().waitForIdleSync();
        
        assertEquals(0.18, activity.getDataController().getTipPercentage());
    }
    
    public void testEditTipInvalid() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.editTipByDialog();
                AlertDialog dialog = activity.getEditTaxOrTipDialog();
                EditText valueEdit = (EditText) dialog.findViewById(R.id.value_edit);
                valueEdit.setText("-0.18");
                Button okay = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        getInstrumentation().waitForIdleSync();
        
        assertEquals(0.2, activity.getDataController().getTipPercentage());
        
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.editTipByDialog();
                AlertDialog dialog = activity.getEditTaxOrTipDialog();
                EditText valueEdit = (EditText) dialog.findViewById(R.id.value_edit);
                valueEdit.setText("1.18");
                Button okay = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        getInstrumentation().waitForIdleSync();
        
        assertEquals(0.2, activity.getDataController().getTipPercentage());        
    }    
//    
//    public void testSaveAndRestoreState() {
//        // TODO: implement
//    }
    
}
