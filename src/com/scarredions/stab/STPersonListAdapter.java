package com.scarredions.stab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class STPersonListAdapter extends ArrayAdapter<String> implements DialogInterface.OnClickListener {

    private STMenuListAdapter menuListAdapter;
    
    public STPersonListAdapter(Context c, int textViewResourceId) {
        super(c, textViewResourceId);
    }
    
    public void setMenuListAdapter(STMenuListAdapter mla) {
        this.menuListAdapter = mla;
    }
    
    public STMenuListAdapter getMenuListAdapter() {
        return menuListAdapter;
    }
    
    // TODO: implement
    public boolean currentPersonHasSelected(int listPosition) {
        return false;
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
