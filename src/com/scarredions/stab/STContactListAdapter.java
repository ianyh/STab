package com.scarredions.stab;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;

public class STContactListAdapter extends SimpleCursorAdapter implements Filterable {

    private ContentResolver mContent;

    public STContactListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        mContent = context.getContentResolver();
    }
    
    @Override
    public String convertToString(Cursor cursor) {
        int index = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        return cursor.getString(index);
    }
    
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) {
            return getFilterQueryProvider().runQuery(constraint);
        }
        
        StringBuilder buffer = null;
        String[] args = null;
        if (constraint != null) {
            buffer = new StringBuilder();
            buffer.append("UPPER(");
            buffer.append(ContactsContract.Contacts.DISPLAY_NAME);
            buffer.append(") GLOB?");
            args = new String[] { constraint.toString().toUpperCase() + "*" };
        }
                
        return mContent.query(ContactsContract.Contacts.CONTENT_URI, 
                STConstants.CONTACTS_PROJECTION, 
                buffer.toString(), 
                args, 
                null);
    }

}
