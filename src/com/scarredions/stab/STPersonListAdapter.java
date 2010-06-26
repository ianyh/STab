package com.scarredions.stab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Gallery;

public class STPersonListAdapter extends ArrayAdapter<String> implements DialogInterface.OnClickListener {

    private STMenuListAdapter menuListAdapter;
    private Gallery personListView;
    
    private STDataController dataController;
    
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
    
    public int getCurrentPersonId() {
        return (int) personListView.getSelectedItemId();
    }
        
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            add(((EditText) d.findViewById(R.id.name_edit)).getText().toString());
            this.notifyDataSetChanged();
        }
    }
    
    // TODO: make entry autocomplete with Contacts
    public void addPersonByDialog() {
        LayoutInflater factory = LayoutInflater.from(this.getContext());
        final View textEntryView = factory.inflate(R.layout.dialog_person_entry, null);
        
        new AlertDialog.Builder(this.getContext())
            .setView(textEntryView)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create().show();
    }

}
