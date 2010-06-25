package com.scarredions.stab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;

public class STMenuListAdapter extends ArrayAdapter<String> implements DialogInterface.OnClickListener {
    
    private STPersonListAdapter pla;
    
    public STMenuListAdapter(Context mContext, int textViewResourceId) {
        super(mContext, textViewResourceId);
    }
    
    public void setPersonListAdapter(STPersonListAdapter pla) {
        this.pla = pla;
    }
    
    public STPersonListAdapter getPersonListAdapter() {
        return pla;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckedTextView ctv = (CheckedTextView) super.getView(position, convertView, parent);
        ctv.setChecked(pla.currentPersonHasSelected(position));
        return ctv;
    }
    
    public void onClick(DialogInterface dialog, int whichButton) {
    	if (whichButton == DialogInterface.BUTTON_POSITIVE) {
	        AlertDialog d = (AlertDialog) dialog;
	        add(((EditText) d.findViewById(R.id.item_id_edit)).getText().toString());
	        this.notifyDataSetChanged();
    	}
    }
    
    public void addMenuItemByDialog() {
	    LayoutInflater factory = LayoutInflater.from(this.getContext());
	    final View textEntryView = factory.inflate(R.layout.dialog_menu_item_entry, null);
	    
	    new AlertDialog.Builder(this.getContext())
	        .setView(textEntryView)
	        .setPositiveButton("OK", this)
	        .setNegativeButton("Cancel", this)
	        .create().show();    	
    }

}
