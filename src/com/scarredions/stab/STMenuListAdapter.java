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
    
    private Context context;
    private STDataController dataController;
    
    private AlertDialog addMenuItemDialog;
    
    public STMenuListAdapter(Context context, STDataController dataController) {
        this.context = context;
        this.dataController = dataController;
    }

    public Context getContext() {
        return context;
    }
    
    public int getCount() {
        return dataController.getMenuItemCount();
    }
    
    public STDataController getDataController() {
        return dataController;
    }
    
    public Object getItem(int position) {
        return dataController.getMenuItemName(position);
    }
        
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout menuItemView;
        TextView menuItemName;
        CheckedTextView menuItemPrice;
        
        if (convertView == null) {
            LayoutInflater factory = LayoutInflater.from(context);
            menuItemView = (LinearLayout) factory.inflate(R.layout.list_item, null);
        } else {
            menuItemView = (LinearLayout) convertView;
        }
        
        menuItemName = (TextView) menuItemView.findViewById(R.id.list_item_name);
        menuItemName.setText(dataController.getMenuItemName(position));
        
        menuItemPrice = (CheckedTextView) menuItemView.findViewById(R.id.list_item_price);
        menuItemPrice.setText(getFormattedPrice(dataController.getMenuItemPrice(position)));
        menuItemPrice.setChecked(dataController.currentPersonHasSelected(position));
        
        return menuItemView;
    }

    public void add(String itemName, Double itemPrice) {
        dataController.addMenuItem(itemName, itemPrice);
    }

    public void addMenuItemByDialog() {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View textEntryView = factory.inflate(R.layout.dialog_menu_item_entry, null);
        
        addMenuItemDialog = new AlertDialog.Builder(getContext())
            .setView(textEntryView)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create();
        
        addMenuItemDialog.show();
    }
    
    @Override
    public void notifyDataSetChanged() {
        dataController.updateMenuListFooter();
        super.notifyDataSetChanged();
    }
        
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            
            EditText entry = (EditText) d.findViewById(R.id.item_name_edit);
            String itemName = entry.getText().toString();
            
            entry = (EditText) d.findViewById(R.id.item_price_edit);
            String itemPrice = entry.getText().toString();
            if (itemName.equals("") || itemPrice.equals("")) {
                addMenuItemDialog = null;
                return;
            }
            
            add(itemName, Double.valueOf(itemPrice));
            
            this.notifyDataSetChanged();
        }
        
        addMenuItemDialog = null;
    }
    
    public static String getFormattedPrice(Double price) {
        return NumberFormat.getCurrencyInstance().format(price.doubleValue());
    }
    
    public AlertDialog getAddMenuItemDialog() {
        return addMenuItemDialog;
    }
    
}
