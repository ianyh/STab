package com.scarredions.stab;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    
    private HashMap<Integer, Set<Integer>> personSelections;
    private int nextId;
    
    public STPersonListAdapter(Context c, int textViewResourceId) {
        super(c, textViewResourceId);
        
        personSelections = new HashMap<Integer, Set<Integer>>();
        nextId = 0;
    }

    public STMenuListAdapter getMenuListAdapter() {
        return menuListAdapter;
    }

    public void setMenuListAdapter(STMenuListAdapter mla) {
        this.menuListAdapter = mla;
    }
    
    public void setPersonListView(Gallery personListView) {
        this.personListView = personListView;
    }

    public void add(String name) {
        super.add(name);
        personSelections.put(Integer.valueOf(nextId), new HashSet<Integer>());
        nextId++;
    }
    
    public int getCurrentPersonId() {
        return (int) personListView.getSelectedItemId();
    }
    public Set<Integer> getCurrentPersonSelections() {
        int currentPersonId = getCurrentPersonId();
        return personSelections.get(Integer.valueOf(currentPersonId));
    }
    
    public int getNumberOfPeopleWithSelection(int menuListPosition) {
        int counter = 0;
        Iterator<Set<Integer>> selectionsIter = personSelections.values().iterator();
        Set<Integer> selections;
        Integer integer;
        
        while(selectionsIter.hasNext()) {
            selections = selectionsIter.next(); 
            Iterator<Integer> integerIter = selections.iterator();
            while(integerIter.hasNext()) {
                integer = integerIter.next();
                if (integer.intValue() == menuListPosition)
                    counter++;
            }
        }
        
        return counter;
    }
    
    public Double getTotal() {
        double total = 0;
//        int position = getCurrentPersonId();
        Set<Integer> selections = getCurrentPersonSelections();
        Iterator<Integer> selectionsIter = selections.iterator();
        Integer integer;
        while(selectionsIter.hasNext()) {
            integer = selectionsIter.next();
            total += menuListAdapter.getItemPrice(integer.intValue()).doubleValue() / 
                getNumberOfPeopleWithSelection(integer.intValue());
        }
        return total;
    }
    
    public void setSelection(int position, boolean checked) {
        Set<Integer> selections = getCurrentPersonSelections();
        if (checked) {
            selections.add(Integer.valueOf(position));
        } else {
            selections.remove(Integer.valueOf(position));
        }
    }
    
    public boolean currentPersonHasSelected(int menuListPosition) {
        Set<Integer> selections = getCurrentPersonSelections();
        return selections.contains(Integer.valueOf(menuListPosition));
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
