package com.scarredions.stab;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;

/**
 * Abstract class defining an API for interacting with Contacts.
 * If Android version is too old it will use the old API instead of the new one.
 *  
 * @author ianyh
 *
 */
public abstract class STContactAccessor {
    private static STContactAccessor sInstance;
   
    /**
     * Instantiate the correct API abstraction depending on version.
     * @return the version appropriate contact accessor.
     */
    public static STContactAccessor getInstance() {
        if (sInstance == null) {
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
            if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
                sInstance = new STContactAccessorOld();
            } else {
                sInstance = new STContactAccessorNew();
            }
        }
        return sInstance;
    }
    
    /**
     * 
     * @return The name of the DISPLAY_NAME column in the Contacts database.
     */
    public abstract String getDisplayNameColumnName();
    
    /**
     * 
     * @param cursor
     * @return the index of the _ID column for the input cursor.
     */
    public abstract int getIdColumnIndex(Cursor cursor);

    /**
     * 
     * @param cursor
     * @return the index of the DISPLAY_NAME column in the input cursor.
     */
    public abstract int getNameColumnIndex(Cursor cursor);
    
    /**
     * Loads a contacts photo from contact ID.
     * @param context
     * @param contactId
     * @return Bitmap of the contact's photo.
     */
    public abstract Bitmap loadContactPhotoFromId(Context context, String contactId);
    
    /**
     * Runs a managed query to grab contacts for auto-completion on adding people. 
     * @param activity Activity managing the query.
     * @return
     */
    public abstract Cursor managedQuery(Activity activity);
    
    /**
     * Runs a constrained query to narrow down auto-complete options.
     * @param content
     * @param constraint
     * @param args
     * @return
     */
    public abstract Cursor query(ContentResolver content, String constraint, String[] args);
    
}
