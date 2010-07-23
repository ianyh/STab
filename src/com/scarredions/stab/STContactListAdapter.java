package com.scarredions.stab;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;

/**
 * Adapter used by the AutoCompleteTextView in the add person dialog.
 * @author ianyh
 *
 */
public class STContactListAdapter extends SimpleCursorAdapter implements Filterable {

    private final STContactAccessor contactAccessor = STContactAccessor.getInstance();
    private ContentResolver mContent;
        
    public STContactListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        mContent = context.getContentResolver();
    }
    
    /**
     * Use DISPLAY_NAME as the String conversion.
     */
    @Override
    public String convertToString(Cursor cursor) {
        int nameIndex = contactAccessor.getNameColumnIndex(cursor);
        
        return cursor.getString(nameIndex);
    }
    
    /**
     * Filter the query based on substrings of names.
     */
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
            buffer.append(contactAccessor.getDisplayNameColumnName());
            buffer.append(") GLOB?");
            args = new String[] { "*" + constraint.toString().toUpperCase() + "*" };
        }
                
        return contactAccessor.query(mContent, buffer.toString(), args);
    }

}
