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

/**
 * ListAdapter for the menu item list.
 * @author ianyh
 *
 */
public class STMenuListAdapter extends BaseAdapter implements DialogInterface.OnClickListener {
        
    /**
     * Static method for formatting a price as a String.
     * @param price
     * @return Currency-formatted String of price.
     */
    public static String getFormattedPrice(Double price) {
        return NumberFormat.getCurrencyInstance().format(price.doubleValue());
    }    
    private Context context;    
    private STDataController dataController;
    private AlertDialog addMenuItemDialog;

    private LinearLayout menuListFooter;
    
    public STMenuListAdapter(Context context, STDataController dataController) {
        this.context = context;
        this.dataController = dataController;
    }
    
    /**
     * Adds a new menu item. 
     * @param itemName
     * @param itemPrice
     */
    public void add(String itemName, Double itemPrice) {
        dataController.addMenuItem(itemName, itemPrice);
    }
    
    /**
     * Opens an AlertDialog for entering a new menu item.
     * 
     * AlertDialog is saved in state for testing purposes.
     */
    public void addMenuItemByDialog() {
        LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(R.layout.dialog_menu_item_entry, null);
        
        addMenuItemDialog = new AlertDialog.Builder(context)
            .setView(textEntryView)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create();
        
        addMenuItemDialog.show();
    }

    /**
     * 
     * @return null if there is no dialog open, otherwise returns the dialog
     */
    public AlertDialog getAddMenuItemDialog() {
        return addMenuItemDialog;
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
    
    public View getMenuListFooter() {
        return menuListFooter;
    }

    /**
     * 
     * @return View within the menu list footer that displays tax 
     */
    public View getTaxView() {
        return menuListFooter.getChildAt(STConstants.MENU_LIST_FOOTER_TAX_POSITION);
    }
    
    /**
     * 
     * @return View within the menu list footer that displays tip
     */
    public View getTipView() {
        return menuListFooter.getChildAt(STConstants.MENU_LIST_FOOTER_TIP_POSITION);
    }

    /**
     * 
     * @return View within the menu list footer that displays total
     */
    public View getTotalView() {
        return menuListFooter.getChildAt(STConstants.MENU_LIST_FOOTER_TOTAL_POSITION);
    }
    
    /**
     * Inflates from list_item.xml
     */
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
        menuItemPrice.setText(STMenuListAdapter.getFormattedPrice(dataController.getMenuItemPrice(position)));
        menuItemPrice.setChecked(dataController.currentPersonHasSelected(position));
        
        return menuItemView;
    }
    
    /**
     * Overridden to force the data controller to update the footer 
     * each time selections change.
     */
    @Override
    public void notifyDataSetChanged() {
        updateMenuListFooter();
        super.notifyDataSetChanged();
    }
    
    /**
     * Click handler for the add menu item dialog.
     * 
     * Fails silently on either empty name or empty price.
     */
    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            
            EditText entry = (EditText) d.findViewById(R.id.item_name_edit);
            String itemName = entry.getText().toString();
            
            entry = (EditText) d.findViewById(R.id.item_price_edit);
            String itemPrice = entry.getText().toString();
            if (itemName.equals("") || itemPrice.equals("") || 
                    Double.valueOf(itemPrice) < 0) {
                addMenuItemDialog = null;
                return;
            }
            
            add(itemName, Double.valueOf(itemPrice));
            
            this.notifyDataSetChanged();
        }
        
        addMenuItemDialog = null;
    }
    
    /**
     * Keep the menu list footer in state for updating
     * @param menuListFooter
     */
    public void setMenuListFooter(LinearLayout menuListFooter) {
        this.menuListFooter = menuListFooter;
    }
    
    /**
     * Sets a new tax percentage and updates the appropriate view to reflect change.
     * @param newTax
     */
    public void setTaxPercentage(double newTax) {
        dataController.setTaxPercentage(newTax);
        TextView taxView = (TextView) ((LinearLayout) getTaxView()).findViewById(R.id.list_footer_text);
        taxView.setText(STConstants.TAX + " (" + dataController.getFormattedTaxPercentage() + ")");
        updateTax();
    }    
    
    /**
     * Sets a new tip percentage and updates the appropriate view to reflect change.
     * @param newTip
     */
    public void setTipPercentage(double newTip) {
        dataController.setTipPercentage(newTip);
        TextView tipView = (TextView) ((LinearLayout) getTipView()).findViewById(R.id.list_footer_text);
        tipView.setText(STConstants.TIP + " (" + dataController.getFormattedTipPercentage() + ")");
        updateTip();
    }
    
    /**
     * Begins chain of menu list footer updates at the bottom.
     * updateMenuListFooter() -> updateTax() -> updateTip() -> updateTotal()
     */
    public void updateMenuListFooter() {
        updateTax();
    }

    /**
     * Updates the tax owed, and then forces an update of tip, which depends on tax.
     * updateTax() -> updateTip() -> updateTotal()
     */
    public void updateTax() {
        TextView tax = (TextView) ((LinearLayout) getTaxView()).findViewById(R.id.list_footer_value);
        tax.setText(STMenuListAdapter.getFormattedPrice(dataController.getPersonTax()));
        updateTip();
    }

    /**
     * Updates the tip owed, and then forces an update of total, which depends on tip.
     * updateTip() -> updateTotal()
     */
    public void updateTip() {
        TextView tip = (TextView) ((LinearLayout) getTipView()).findViewById(R.id.list_footer_value);
        tip.setText(STMenuListAdapter.getFormattedPrice(dataController.getPersonTip()));
        updateTotal();
    }

    /**
     * Updates the total owed.
     * updateTotal()
     */
    public void updateTotal() {
        TextView total = (TextView) ((LinearLayout) getTotalView()).findViewById(R.id.list_footer_value);
        total.setText(STMenuListAdapter.getFormattedPrice(dataController.getPersonTotal() + dataController.getPersonTax() + dataController.getPersonTip()));
    }
    
}
