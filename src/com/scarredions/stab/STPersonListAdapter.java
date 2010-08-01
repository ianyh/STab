package com.scarredions.stab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * ListAdapter for the person list gallery.
 * @author ianyh
 *
 */
public class STPersonListAdapter extends BaseAdapter implements DialogInterface.OnClickListener, AdapterView.OnItemClickListener {

    private STab activity;
    
    private STDataController dataController;
    private SimpleCursorAdapter contactsAutoCompleteAdapter;
    
    private AlertDialog addPersonDialog;
    
    public STPersonListAdapter(STab activity, STDataController dataController) {
        this.activity = activity;
        this.dataController = dataController;
    }
    
    /**
     * Adds a person not taken from contacts
     * @param name
     */
    public void add(String name) {
        add(name, STConstants.PERSON_NULL_ID);
    }
    
    /**
     * Adds a person with name and id from the contacts table.
     * @param name
     * @param id
     */
    public void add(String name, String id) {
        dataController.addPerson(name, id);
        activity.notifyDataSetChanged();
    }
    
    public void add(String name, String id, int personId)
    {
        if (personId < 0) {
            add(name, id);
        } else {
            dataController.updatePerson(personId, name, id);
        }
    }
    
    /**
     * Opens an AlertDialog for adding a person to the list.
     * 
     * The dialog itself is saved in internal state for testing purposes.
     */
    public void addPersonByDialog() {
        LayoutInflater factory = LayoutInflater.from(activity);
        LinearLayout dialogLayout = (LinearLayout) factory.inflate(R.layout.dialog_person_entry, null);
        AutoCompleteTextView textEntryView = (AutoCompleteTextView) dialogLayout.findViewById(R.id.name_edit);
        textEntryView.setAdapter(contactsAutoCompleteAdapter);
        textEntryView.setOnItemClickListener(this);
        
        addPersonDialog = new AlertDialog.Builder(activity)
            .setView(dialogLayout)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create();
        
        addPersonDialog.show();
    }
    
    public void editPersonByDialog(int personId)
    {
        LayoutInflater factory = LayoutInflater.from(activity);
        LinearLayout dialogLayout = (LinearLayout) factory.inflate(R.layout.dialog_person_entry, null);
        AutoCompleteTextView textEntryView = (AutoCompleteTextView) dialogLayout.findViewById(R.id.name_edit);
        textEntryView.setAdapter(contactsAutoCompleteAdapter);
        textEntryView.setOnItemClickListener(this);
        dataController.setAutoCompletedContact(dataController.getPersonName(personId), dataController.getPersonContactId(personId));
        textEntryView.setText(dataController.getPersonName(personId));
        
        TextView personIdTextView = (TextView) dialogLayout.findViewById(R.id.person_id_hidden);
        personIdTextView.setText(String.valueOf(personId));
        
        new AlertDialog.Builder(activity)
            .setView(dialogLayout)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create()
            .show();
    }

    /**
     * 
     * @return null if there is no open dialog, otherwise returns the dialog
     */
    public AlertDialog getAddPersonDialog() {
        return addPersonDialog;
    }
    
    public int getCount() {
        return dataController.getPersonCount();
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
    
    /**
     * Inflates from contact.xml
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout;
        if (convertView == null) {
            LayoutInflater factory = LayoutInflater.from(activity);
            layout = (LinearLayout) factory.inflate(R.layout.contact, null);
            ImageView contactPhotoView = (ImageView) layout.findViewById(R.id.contact_image);
            TextView contactNameView = (TextView) layout.findViewById(R.id.contact_name);
            
            Bitmap contactPhoto = dataController.getPersonPhoto(position);
            if (contactPhoto != null) {
                contactPhotoView.setImageBitmap(contactPhoto);
            }
            contactNameView.setText(dataController.getPersonName(position));
        } else {
            layout = (LinearLayout) convertView;
        }
        
        TextView contactTotal = (TextView) layout.findViewById(R.id.contact_total);
        contactTotal.setText(dataController.getPersonTotal(position));
        
        return layout;
    }
    
    /**
     * Click handler for add person dialog.
     * 
     * Fails silently if the name is empty.
     */
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            AutoCompleteTextView nameView = (AutoCompleteTextView) d.findViewById(R.id.name_edit);
            String name = nameView.getText().toString();
            if (name.equals("")) {
                addPersonDialog = null;
                return;
            }
            String contactId = dataController.getAndClearAutoCompletedContactId(name);
            if (contactId == null) {
                contactId = STConstants.PERSON_NULL_ID;
            }
            
            String personId = ((TextView) d.findViewById(R.id.person_id_hidden)).getText().toString();
            
            add(name, contactId, Integer.valueOf(personId).intValue());
            activity.notifyDataSetChanged();
        }
        
        addPersonDialog = null;
    }
    
    public void setContactsAutoCompleteAdapter(SimpleCursorAdapter adapter) {
        contactsAutoCompleteAdapter = adapter;
    }

    public void onItemClick(AdapterView<?> autoCompleteList, View autoCompleteItem, int listPosition, long contactId) {
        String contactName = ((TextView) autoCompleteItem).getText().toString();
        dataController.setAutoCompletedContact(contactName, String.valueOf(contactId));
    }
    
}
