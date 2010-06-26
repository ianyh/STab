package com.scarredions.stab;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class STMenuListAdapter extends BaseAdapter implements DialogInterface.OnClickListener {
    
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private STPersonListAdapter personListAdapter;
    private Context mContext;
    private ArrayList<String> menuItemNames;
    private ArrayList<Double> menuItemPrices;
    
    public STMenuListAdapter(Context mContext) {
        this.mContext = mContext;
        menuItemNames = new ArrayList<String>();
        menuItemPrices = new ArrayList<Double>();
    }
    
    public STPersonListAdapter getPersonListAdapter() {
        return personListAdapter;
    }
    
    public int getCount() {
        return menuItemNames.size();
    }
    
    public long getItemId(int position) {
        return position;
    }
    
    public Context getContext() {
        return mContext;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater factory = LayoutInflater.from(getContext());
            LinearLayout menuItem = (LinearLayout) factory.inflate(R.layout.list_item, null);
            ((TextView) menuItem.getChildAt(0)).setText(this.menuItemNames.get(position));
            ((CheckedTextView) menuItem.getChildAt(1)).setText(getFormattedPrice(this.menuItemPrices.get(position)));
            return menuItem;
        } else {
            return convertView;
        }
    }

    public Object getItem(int position) {
        return menuItemNames.get(position);
    }

    public void setPersonListAdapter(STPersonListAdapter pla) {
        this.personListAdapter = pla;
    }
        
    public void add(String itemName, Double itemPrice) {
        menuItemNames.add(itemName);
        menuItemPrices.add(itemPrice);
    }
    
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            
            EditText entry = (EditText) d.findViewById(R.id.item_name_edit);
            String itemName = entry.getText().toString();
            
            entry = (EditText) d.findViewById(R.id.item_price_edit);
            String itemPrice = entry.getText().toString();
            
            add(itemName, Double.valueOf(itemPrice));
            
            this.notifyDataSetChanged();
        }
    }
    
    public String getFormattedPrice(Double price) {
        return formatter.format(price.doubleValue());
    }
    
    public void addMenuItemByDialog() {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View textEntryView = factory.inflate(R.layout.dialog_menu_item_entry, null);
        
        new AlertDialog.Builder(getContext())
            .setView(textEntryView)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create().show();        
    }

}
