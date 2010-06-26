package com.scarredions.stab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;

public class STPersonListAdapter extends ArrayAdapter<String> implements DialogInterface.OnClickListener {

    private STMenuListAdapter menuListAdapter;
    private Gallery personListView;
    
    private STDataController dataController;
    private SimpleCursorAdapter contactsAutocompleteAdapter;
    
    public STPersonListAdapter(Context c, int textViewResourceId, STDataController dataController) {
        super(c, textViewResourceId);
        this.dataController = dataController;
    }

    public STMenuListAdapter getMenuListAdapter() {
        return menuListAdapter;
    }
    
    public STDataController getDataController() {
        return dataController;
    }

    public void setMenuListAdapter(STMenuListAdapter mla) {
        this.menuListAdapter = mla;
    }
    
    public void setPersonListView(Gallery personListView) {
        this.personListView = personListView;
    }

    public void add(String name) {
        super.add(name);
        dataController.addPerson(name);
    }

    public void setContactsAutocompleteAdapter(SimpleCursorAdapter adapter) {
        contactsAutocompleteAdapter = adapter;
    }
    
    public int getCurrentPersonId() {
        return (int) personListView.getSelectedItemId();
    }
        
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            add(((AutoCompleteTextView) d.findViewById(R.id.name_edit)).getText().toString());
            this.notifyDataSetChanged();
        }
    }
    
    public void addPersonByDialog() {
        LayoutInflater factory = LayoutInflater.from(this.getContext());
        LinearLayout dialogLayout = (LinearLayout) factory.inflate(R.layout.dialog_person_entry, null);
        AutoCompleteTextView textEntryView = (AutoCompleteTextView) dialogLayout.findViewById(R.id.name_edit);
        textEntryView.setAdapter(contactsAutocompleteAdapter);
        
        new AlertDialog.Builder(this.getContext())
            .setView(dialogLayout)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create().show();
    }

}
