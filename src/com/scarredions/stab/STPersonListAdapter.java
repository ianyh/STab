package com.scarredions.stab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

    private Context context;
    private STMenuListAdapter menuListAdapter;
    private Gallery personListView;
    
    private STDataController dataController;
    private SimpleCursorAdapter contactsAutoCompleteAdapter;
    
    public STPersonListAdapter(Context context, STDataController dataController) {
        this.context = context;
        this.dataController = dataController;
    }
        
    public Context getContext() {
        return context;
    }

    public int getCount() {
        return dataController.getPersonCount();
    }
    
    public int getCurrentPersonId() {
        return (int) personListView.getSelectedItemId();
    }
    
    public STDataController getDataController() {
        return dataController;
    }
    
    public Object getItem(int position) {
        return dataController.getPersonName(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public STMenuListAdapter getMenuListAdapter() {
        return menuListAdapter;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater factory = LayoutInflater.from(context);
            LinearLayout layout = (LinearLayout) factory.inflate(R.layout.contact, null);
            ImageView contactPhotoView = (ImageView) layout.findViewById(R.id.contact_image);
            TextView contactNameView = (TextView) layout.findViewById(R.id.contact_name);
            
            Bitmap contactPhoto = dataController.getPersonPhoto(position);
            if (contactPhoto != null) {
                contactPhotoView.setImageBitmap(contactPhoto);
            }
            contactNameView.setText(dataController.getPersonName(position));
            return layout;
        } else {
            return convertView;
        }
    }

    public void add(String name) {
        add(name, STConstants.PERSON_NULL_ID);
    }
    
    public void add(String name, String id) {
        dataController.addPerson(name, id);
    }
        
    public void addPersonByDialog() {
        LayoutInflater factory = LayoutInflater.from(context);
        LinearLayout dialogLayout = (LinearLayout) factory.inflate(R.layout.dialog_person_entry, null);
        AutoCompleteTextView textEntryView = (AutoCompleteTextView) dialogLayout.findViewById(R.id.name_edit);
        textEntryView.setAdapter(contactsAutoCompleteAdapter);
        
        new AlertDialog.Builder(context)
            .setView(dialogLayout)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create().show();
    }
        
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            String name = ((AutoCompleteTextView) d.findViewById(R.id.name_edit)).getText().toString();
            String contactId = dataController.getAndClearAutoCompletedContactId();
            add(name, contactId);
            this.notifyDataSetChanged();
        }
    }

    public void setContactsAutoCompleteAdapter(SimpleCursorAdapter adapter) {
        contactsAutoCompleteAdapter = adapter;
    }
    
    public void setMenuListAdapter(STMenuListAdapter mla) {
        this.menuListAdapter = mla;
    }
    
    public void setPersonListView(Gallery personListView) {
        this.personListView = personListView;
    }
    
}
