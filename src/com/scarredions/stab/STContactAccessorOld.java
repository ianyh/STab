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

/**
 * Contacts accessor using the old Contacts.People API.
 * @author ianyh
 *
 */
@SuppressWarnings("deprecation")
public class STContactAccessorOld extends STContactAccessor {

    /**
     * Selection for query to contacts for auto-complete
     */
    public static final String[] CONTACTS_PROJECTION = new String[] {
        BaseColumns._ID,
        PeopleColumns.DISPLAY_NAME
    };

    /**
     * 
     * @return The name of the DISPLAY_NAME column in the Contacts database.
     */
    @Override
    public String getDisplayNameColumnName() {
        return PeopleColumns.DISPLAY_NAME;
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
        return cursor.getColumnIndex(PeopleColumns.DISPLAY_NAME);
    }

    /**
     * Loads a contacts photo from contact ID.
     * @param context
     * @param contactId
     * @return Bitmap of the contact's photo.
     */
    @Override
    public Bitmap loadContactPhotoFromId(Context context, String contactId) {
        InputStream contactPhotoStream = Contacts.People.openContactPhotoInputStream(
                context.getContentResolver(),
                ContentUris.withAppendedId(
                        Contacts.People.CONTENT_URI,
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
        return activity.managedQuery(
                Contacts.People.CONTENT_URI,
                CONTACTS_PROJECTION,
                null,
                null,
                PeopleColumns.DISPLAY_NAME);
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
        return content.query(
                Contacts.People.CONTENT_URI,
                CONTACTS_PROJECTION,
                constraint,
                args,
                PeopleColumns.DISPLAY_NAME);
    }

}
