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
import android.provider.Contacts;
import android.provider.Contacts.PeopleColumns;

@SuppressWarnings("deprecation")
public class STContactAccessorOld extends STContactAccessor {

    /**
     * Selection for query to contacts for auto-complete
     */
    public static final String[] CONTACTS_PROJECTION = new String[] {
        BaseColumns._ID,
        PeopleColumns.DISPLAY_NAME
    };
    
    @Override
    public Cursor managedQuery(Activity activity) {
        return activity.managedQuery(
                Contacts.People.CONTENT_URI,
                CONTACTS_PROJECTION,
                null,
                null,
                PeopleColumns.DISPLAY_NAME);
    }

    @Override
    public Bitmap loadContactPhotoFromId(Context context, String contactId) {
        InputStream contactPhotoStream = Contacts.People.openContactPhotoInputStream(
                context.getContentResolver(),
                ContentUris.withAppendedId(
                        Contacts.People.CONTENT_URI,
                        Double.valueOf(contactId).longValue()));
        return BitmapFactory.decodeStream(contactPhotoStream);
    }

    @Override
    public int getIdColumnIndex(Cursor cursor) {
        return cursor.getColumnIndex(BaseColumns._ID);
    }

    @Override
    public int getNameColumnIndex(Cursor cursor) {
        return cursor.getColumnIndex(PeopleColumns.DISPLAY_NAME);
    }

    @Override
    public String getDisplayNameColumnName() {
        return PeopleColumns.DISPLAY_NAME;
    }

    @Override
    public Cursor query(ContentResolver content, String constraint, String[] args) {
        return content.query(
                Contacts.People.CONTENT_URI,
                CONTACTS_PROJECTION,
                constraint,
                args,
                PeopleColumns.DISPLAY_NAME);
    }

}
