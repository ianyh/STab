package com.scarredions.stab;

import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

public class STContactAccessorNew extends STContactAccessor {
    
    /**
     * Selection for query to contacts for auto-complete
     */
    public static final String[] CONTACTS_PROJECTION = new String[] {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME
    };

    @Override
    public Cursor managedQuery(Activity activity) {
        return activity.managedQuery(ContactsContract.Contacts.CONTENT_URI, 
                CONTACTS_PROJECTION, 
                null, 
                null, 
                ContactsContract.Contacts.DISPLAY_NAME);        
    }

    @Override
    public Bitmap loadContactPhotoFromId(Context context, String contactId) {
        InputStream contactPhotoStream = ContactsContract.Contacts.openContactPhotoInputStream(
                context.getContentResolver(),
                ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        Double.valueOf(contactId).longValue()));
        return BitmapFactory.decodeStream(contactPhotoStream);
    }

    @Override
    public int getIdColumnIndex(Cursor cursor) {
        return cursor.getColumnIndex(ContactsContract.Contacts._ID);
    }

    @Override
    public int getNameColumnIndex(Cursor cursor) {
        return cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
    }

    @Override
    public String getDisplayNameColumnName() {
        return ContactsContract.Contacts.DISPLAY_NAME;
    }

    @Override
    public Cursor query(ContentResolver content, String constraint, String[] args) {
        return content.query(ContactsContract.Contacts.CONTENT_URI, 
                CONTACTS_PROJECTION, 
                constraint,
                args,
                ContactsContract.Contacts.DISPLAY_NAME);
    }

}
