package com.udacity.firebase.shoppinglistplusplus.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by New Owner on 3/6/2017.
 */

public class ShoppingListItem {
    private String itemName;
    private String itemOwner;
    private boolean itemBought;
    private String itemBoughtBy;

    public boolean isItemBought() {
        return itemBought;
    }

    public String getItemBoughtBy() {
        return itemBoughtBy;
    }

    public ShoppingListItem() {

    }

    public ShoppingListItem(String itemName, String itemOwner) {
        this.itemName = itemName;
        this.itemOwner = itemOwner;
        this.itemBought = false;
        this.itemBoughtBy = null;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemOwner() {
        return itemOwner;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("itemName", itemName);
        result.put("itemOwner", itemOwner);
        return result;
    }
}
