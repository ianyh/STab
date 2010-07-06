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
    
    private void clickButton(final Button button) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                button.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
    }
    
    public void testAddPerson() {
        clickButton((Button) activity.findViewById(R.id.add_person_button));
        
        assertEquals(true, activity.getPersonListAdapter().getAddPersonDialog() != null);
        
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog addPersonDialog = activity.getPersonListAdapter().getAddPersonDialog();
                EditText nameEdit = (EditText) addPersonDialog.findViewById(R.id.name_edit);
                nameEdit.setText("test");
                Button okay = addPersonDialog.getButton(DialogInterface.BUTTON_POSITIVE);                
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
        
        assertEquals(activity.getDataController().getPersonName(1), "test");
        assertEquals(activity.getDataController().getPersonPhoto(1), null);
    }
    
    public void testAddMenuItem() {
        clickButton((Button) activity.findViewById(R.id.add_menu_item_button));
        
        assertEquals(true, activity.getMenuListAdapter().getAddMenuItemDialog() != null);
        
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog addMenuItemDialog = activity.getMenuListAdapter().getAddMenuItemDialog();
                EditText nameEdit = (EditText) addMenuItemDialog.findViewById(R.id.item_name_edit);
                nameEdit.setText("test");
                EditText priceEdit = (EditText) addMenuItemDialog.findViewById(R.id.item_price_edit);
                priceEdit.setText("1.99");
                Button okay = addMenuItemDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okay.requestFocus();
            }
        });
        
        getInstrumentation().waitForIdleSync();
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
        
        assertEquals(activity.getDataController().getMenuItemName(0), "test");
        assertEquals(activity.getDataController().getMenuItemPrice(0), Double.valueOf(1.99));
    }
    
}
