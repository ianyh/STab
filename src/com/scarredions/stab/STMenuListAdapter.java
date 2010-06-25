package com.scarredions.stab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class STMenuListAdapter extends ArrayAdapter<String> {
    
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
    
}
