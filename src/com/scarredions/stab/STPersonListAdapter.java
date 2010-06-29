package com.scarredions.stab;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class STPersonListAdapter extends BaseAdapter implements DialogInterface.OnClickListener {

    private Context mContext;
    
    private STMenuListAdapter menuListAdapter;
    private Gallery personListView;
    
    private STDataController dataController;
    private SimpleCursorAdapter contactsAutoCompleteAdapter;
    
    private ArrayList<Bitmap> contactPhotos;
    private ArrayList<String> contactNames;
    private ContentResolver content;
    
    public STPersonListAdapter(Context c, STDataController dataController) {
        mContext = c;
        content = c.getContentResolver();
        this.dataController = dataController;
        contactPhotos = new ArrayList<Bitmap>();
        contactNames = new ArrayList<String>();
    }

    public Context getContext() {
        return mContext;
    }
    
    public STMenuListAdapter getMenuListAdapter() {
        return menuListAdapter;
    }
    
    public STDataController getDataController() {
        return dataController;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater factory = LayoutInflater.from(getContext());
            LinearLayout layout = (LinearLayout) factory.inflate(R.layout.contact, null);
            ImageView contactPhotoView = (ImageView) layout.findViewById(R.id.contact_image);
            TextView contactNameView = (TextView) layout.findViewById(R.id.contact_name);
            
            Bitmap contactPhoto = contactPhotos.get(position);
            if (contactPhoto != null)
                contactPhotoView.setImageBitmap(contactPhoto);
            contactNameView.setText(contactNames.get(position));
            return layout;
        } else {
            return convertView;
        }
    }
    
    public void setMenuListAdapter(STMenuListAdapter mla) {
        this.menuListAdapter = mla;
    }
    
    public void setPersonListView(Gallery personListView) {
        this.personListView = personListView;
    }

    public void add(String name) {
        contactNames.add(name);
        dataController.addPerson(name);
    }
    
    public void addPhoto(Bitmap photo) {
        contactPhotos.add(photo);
    }

    public void setContactsAutocompleteAdapter(SimpleCursorAdapter adapter) {
        contactsAutoCompleteAdapter = adapter;
    }
    
    public int getCurrentPersonId() {
        return (int) personListView.getSelectedItemId();
    }
        
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            String name = ((AutoCompleteTextView) d.findViewById(R.id.name_edit)).getText().toString();
            add(name);
            String contactId = dataController.getSelectedContactId();
            contactPhotos.add(getBitmapFromId(contactId));
            this.notifyDataSetChanged();
        }
    }
    
    public Bitmap getBitmapFromId(String contactId) {
        InputStream contactPhotoStream = ContactsContract.Contacts.openContactPhotoInputStream(
                content, 
                ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        Double.valueOf(contactId).longValue()));
        return BitmapFactory.decodeStream(contactPhotoStream);
    }
    
    public void addPersonByDialog() {
        LayoutInflater factory = LayoutInflater.from(this.getContext());
        LinearLayout dialogLayout = (LinearLayout) factory.inflate(R.layout.dialog_person_entry, null);
        AutoCompleteTextView textEntryView = (AutoCompleteTextView) dialogLayout.findViewById(R.id.name_edit);
        textEntryView.setAdapter(contactsAutoCompleteAdapter);
        
        new AlertDialog.Builder(this.getContext())
            .setView(dialogLayout)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create().show();
    }

    public int getCount() {
        return contactNames.size();
    }

    public Object getItem(int position) {
        return contactNames.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}
