package com.udacity.firebase.shoppinglistplusplus.model;

/**
 * Created by New Owner on 3/6/2017.
 */

public class ShoppingListItem {
    private String name;
    private String owner;

    public ShoppingListItem() {

    }

    public ShoppingListItem(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }
}
