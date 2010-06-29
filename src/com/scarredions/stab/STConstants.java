package com.scarredions.stab;

import android.provider.ContactsContract;

public class STConstants {
    /**
     * Constants for switching on menu selection
     */
    public static final int MENU_EDIT_TAX = 0;
    public static final int MENU_EDIT_TIP = 1;
    
    /**
     * Positions of views in the menu list footer
     */
    public static final int MENU_LIST_FOOTER_TAX_POSITION = 0;
    public static final int MENU_LIST_FOOTER_TIP_POSITION = 1;
    public static final int MENU_LIST_FOOTER_TOTAL_POSITION = 2;
    
    /**
     * Strings for display of tax and tip
     * Also used for switching on tax/tip entry dialog
     */
    public static final String TAX = "Tax";
    public static final String TIP = "Tip";
    
    /**
     * Selection for query to contacts for auto-complete
     */
    public static final String[] CONTACTS_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME
    };
    
}