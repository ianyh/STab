package com.scarredions.stab;

import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Contact accessor using the new ContactsContract API.
 * @author ianyh
 *
 */
public class STContactAccessorNew extends STContactAccessor {
    
    /**
     * Selection for query to contacts for auto-complete
     */
    public static final String[] CONTACTS_PROJECTION = new String[] {
        BaseColumns._ID,
        ContactsContract.Contacts.DISPLAY_NAME
    };

    /**
     * 
     * @return The name of the DISPLAY_NAME column in the Contacts database.
     */
    @Override
    public String getDisplayNameColumnName() {
        return ContactsContract.Contacts.DISPLAY_NAME;
    }

    /**
     * 
     * @param cursor
     * @return the index of the _ID column for the input cursor.
     */
    @Override
    public int getIdColumnIndex(Cursor cursor) {
        return cursor.getColumnIndex(BaseColumns._ID);
    }

    /**
     * 
     * @param cursor
     * @return the index of the DISPLAY_NAME column in the input cursor.
     */
    @Override
    public int getNameColumnIndex(Cursor cursor) {
        return cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
    }

    /**
     * Loads a contacts photo from contact ID.
     * @param context
     * @param contactId
     * @return Bitmap of the contact's photo.
     */
    @Override
    public Bitmap loadContactPhotoFromId(Context context, String contactId) {
        InputStream contactPhotoStream = ContactsContract.Contacts.openContactPhotoInputStream(
                context.getContentResolver(),
                ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        Double.valueOf(contactId).longValue()));
        return BitmapFactory.decodeStream(contactPhotoStream);
    }

    /**
     * Runs a managed query to grab contacts for auto-completion on adding people. 
     * @param activity Activity managing the query.
     * @return
     */
    @Override
    public Cursor managedQuery(Activity activity) {
        return activity.managedQuery(ContactsContract.Contacts.CONTENT_URI,
                CONTACTS_PROJECTION,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME);
    }

    /**
     * Runs a constrained query to narrow down auto-complete options.
     * @param content
     * @param constraint
     * @param args
     * @return
     */
    @Override
    public Cursor query(ContentResolver content, String constraint, String[] args) {
        return content.query(ContactsContract.Contacts.CONTENT_URI,
                CONTACTS_PROJECTION,
                constraint,
                args,
                ContactsContract.Contacts.DISPLAY_NAME);
    }

}
