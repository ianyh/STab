package com.scarredions.stab;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;

public class STContactListAdapter extends SimpleCursorAdapter implements Filterable {

    private ContentResolver mContent;
    private STDataController dataController;

    public STContactListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, STDataController dataController) {        
        super(context, layout, c, from, to);
        mContent = context.getContentResolver();
        this.dataController = dataController;
    }
        
    @Override
    public String convertToString(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        
        dataController.setAutoCompletedContactId(cursor.getString(idIndex));
        return cursor.getString(nameIndex);
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
