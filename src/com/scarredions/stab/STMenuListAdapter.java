package com.scarredions.stab;

import java.text.NumberFormat;

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

    private Context mContext;
    private STDataController dataController;
    public STMenuListAdapter(Context mContext, STDataController dataController) {
        this.mContext = mContext;
        this.dataController = dataController;
    }
    
    public STDataController getDataController() {
        return dataController;
    }
    
    public int getCount() {
        return dataController.getMenuItemCount();
    }
    
    public long getItemId(int position) {
        return position;
    }
    
    public Context getContext() {
        return mContext;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout menuItemView;
        TextView menuItemName;
        CheckedTextView menuItemPrice;
        
        if (convertView == null) {
            LayoutInflater factory = LayoutInflater.from(getContext());            
            menuItemView = (LinearLayout) factory.inflate(R.layout.list_item, null);
        } else {
            menuItemView = (LinearLayout) convertView;
        }
        
        menuItemName = (TextView) menuItemView.getChildAt(0);
        menuItemName.setText(dataController.getMenuItemName(position));
        
        menuItemPrice = (CheckedTextView) menuItemView.getChildAt(1);
        menuItemPrice.setText(getFormattedPrice(dataController.getMenuItemPrice(position)));
        menuItemPrice.setChecked(dataController.currentPersonHasSelected(position));
        
        return menuItemView;
    }

    public Object getItem(int position) {
        return dataController.getMenuItemName(position);
    }
    
    public void add(String itemName, Double itemPrice) {
        dataController.addMenuItem(itemName, itemPrice);
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
