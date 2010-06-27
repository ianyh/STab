package com.scarredions.stab;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class STDataController implements OnClickListener {
    
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    // TODO: get this from location?
    private double tax = 0.08;
    private double tip = 0.2;
    
    private HashMap<Integer, HashSet<Integer>> personToSelections;
    private int currentPersonId;
    private int nextPersonId = 0;
    
    private ArrayList<MenuItem> menuItems;
    
    private LinearLayout menuListFooter;
    
    public STDataController() {
        personToSelections = new HashMap<Integer, HashSet<Integer>>();
        menuItems = new ArrayList<MenuItem>();
    }
    
    public HashSet<Integer> getPersonSelections(int personId) {
        return personToSelections.get(Integer.valueOf(personId));
    }
    
    public HashSet<Integer> getCurrentPersonSelections() {
        return getPersonSelections(currentPersonId);
    }
    
    public Iterator<HashSet<Integer>> getPersonSelectionsIter() {
        return personToSelections.values().iterator();
    }
    
    public int getCurrentPersonId() {
        return currentPersonId;
    }
    
    public boolean currentPersonHasSelected(int menuListPosition) {
        Set<Integer> selections = getCurrentPersonSelections();
        return selections.contains(Integer.valueOf(menuListPosition));
    }
    
    public int getNumberOfPeopleWithSelection(int menuListPosition) {
        int counter = 0;
        Iterator<HashSet<Integer>> selectionsIter = getPersonSelectionsIter();
        HashSet<Integer> selections;
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
    
    public double getTaxPercentage() {
        return tax;
    }
    
    public double getTipPercentage() {
        return tip;
    }
    
    public String getFormattedTaxPercentage() {
        return NumberFormat.getPercentInstance().format(getTaxPercentage());
    }
    
    public String getFormattedTipPercentage() {
        return NumberFormat.getPercentInstance().format(getTipPercentage());
    }
    
    public Double getTax() {
        return getTotal() * tax;
    }
    
    public Double getTip() {
        return (getTotal() + getTax()) * tip;
    }
    
    public Double getTotal() {
        double total = 0;
        
        Iterator<MenuItem> menuItemsIter = menuItems.iterator();
        MenuItem menuItem;
        
        while(menuItemsIter.hasNext()) {
            menuItem = menuItemsIter.next();
            total += menuItem.getPrice().doubleValue();
        }
        
        return Double.valueOf(total);
    }
    
    public Double getPersonTax() {
        return getTax() / personToSelections.size();
    }
    
    public Double getPersonTip() {
        return getTip() / personToSelections.size();
    }
    
    public Double getPersonTotal() {
        double total = 0;
        Set<Integer> selections = getCurrentPersonSelections();
        Iterator<Integer> selectionsIter = selections.iterator();
        Integer integer;
        while(selectionsIter.hasNext()) {
            integer = selectionsIter.next();
            total += getMenuItemPrice(integer.intValue()).doubleValue() /
                getNumberOfPeopleWithSelection(integer.intValue());
        }
        return total;
    }
    
    public MenuItem getMenuItem(int position) {
        return menuItems.get(position);
    }
    
    public String getMenuItemName(int position) {
        return menuItems.get(position).getName();
    }
    
    public Double getMenuItemPrice(int position) {
        return menuItems.get(position).getPrice();
    }
    
    public String getFormattedPrice(Double price) {
        return formatter.format(price.doubleValue());
    }
    
    public int getMenuItemCount() {
        return menuItems.size();
    }
    
    public View getMenuListFooter() {
        return menuListFooter;
    }
    
    public View getTaxView() {
        return menuListFooter.getChildAt(STConstants.MENU_LIST_FOOTER_TAX_POSITION);
    }
    
    public View getTipView() {
        return menuListFooter.getChildAt(STConstants.MENU_LIST_FOOTER_TIP_POSITION);
    }
    
    public View getTotalView() {
        return menuListFooter.getChildAt(STConstants.MENU_LIST_FOOTER_TOTAL_POSITION);
    }
    
    public void addPerson(String name) {
        personToSelections.put(Integer.valueOf(nextPersonId), new HashSet<Integer>());
        nextPersonId++;
        updateFooter();
    }
    
    public void addMenuItem(String name, Double price) {
        menuItems.add(new MenuItem(name, price));
    }

    public void setMenuListFooter(LinearLayout footerView) {
        menuListFooter = footerView;
    }
        
    public void setCurrentPersonId(int personId) {
        currentPersonId = personId;
    }
    
    public void setTaxPercentage(double newTax) {
        tax = newTax;
        TextView taxView = (TextView) ((LinearLayout) getTaxView()).findViewById(R.id.list_footer_text);
        taxView.setText(STConstants.TAX + " (" + getFormattedTaxPercentage() + ")");
        updateTax();
    }
    
    public void setTipPercentage(double newTip) {
        tip = newTip;
        TextView tipView = (TextView) ((LinearLayout) getTipView()).getChildAt(R.id.list_footer_text);
        tipView.setText(STConstants.TIP + " (" + getFormattedTipPercentage() + ")");
        updateTip();
    }
    
    public void setSelection(int menuListPosition, boolean checked) {
        HashSet<Integer> selections = getCurrentPersonSelections();
        if (checked) {
            selections.add(Integer.valueOf(menuListPosition));
        } else {
            selections.remove(Integer.valueOf(menuListPosition));
        }
    }

    public void updateFooter() {
        updateTax();
    }
    
    public void updateTax() {
        TextView tax = (TextView) ((LinearLayout) getTaxView()).findViewById(R.id.list_footer_value);
        tax.setText(getFormattedPrice(getPersonTax()));
        updateTip();
    }
    
    public void updateTip() {
        TextView tip = (TextView) ((LinearLayout) getTipView()).findViewById(R.id.list_footer_value);
        tip.setText(getFormattedPrice(getPersonTip()));
        updateTotal();
    }
    
    public void updateTotal() {
        TextView total = (TextView) ((LinearLayout) getTotalView()).findViewById(R.id.list_footer_value);
        total.setText(getFormattedPrice(getPersonTotal() + getPersonTax() + getPersonTip()));
    }
    
    public void editTaxByDialog() {
        editByDialog(STConstants.TAX, Double.valueOf(getTaxPercentage()));
    }
    
    public void editTipByDialog() {
        editByDialog(STConstants.TIP, Double.valueOf(getTipPercentage()));
    }

    public void onClick(DialogInterface dialog, int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            AlertDialog d = (AlertDialog) dialog;
            String type = ((TextView) d.findViewById(R.id.value_view)).getText().toString();            
            String value = ((EditText) d.findViewById(R.id.value_edit)).getText().toString();
            if (type.equals("Tax")) {
                setTaxPercentage(Double.valueOf(value));
            } else if (type.equals("Tip")) {
                setTipPercentage(Double.valueOf(value));
            }
        }        
    }
    
    private void editByDialog(String type, Double value) {
        LayoutInflater factory = LayoutInflater.from(menuListFooter.getContext());
        LinearLayout dialogLayout = (LinearLayout) factory.inflate(R.layout.dialog_tax_or_tip_entry, null);
        ((TextView) dialogLayout.findViewById(R.id.value_view)).setText(type);
        ((EditText) dialogLayout.findViewById(R.id.value_edit)).setText(value.toString());
        
        new AlertDialog.Builder(menuListFooter.getContext())
            .setView(dialogLayout)
            .setPositiveButton("OK", this)
            .setNegativeButton("Cancel", this)
            .create().show();
        
    }
    
    public class MenuItem {
        private String name;
        private Double price;
        
        public MenuItem(String name, Double price) {
            this.name = name;
            this.price = price;
        }
        
        public String getName() {
            return name;
        }
        
        public Double getPrice() {
            return price;
        }
    }

}
