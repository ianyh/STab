package com.scarredions.stab;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class STContactListAdapter extends SimpleCursorAdapter {
    
    public static int CONTACT_NAME_COLUMN = 1;

    public STContactListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @Override
    public String convertToString(Cursor cursor) {
        return cursor.getString(CONTACT_NAME_COLUMN);
    }

}
