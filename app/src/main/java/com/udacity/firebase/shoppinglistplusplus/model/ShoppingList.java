package com.udacity.firebase.shoppinglistplusplus.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by New Owner on 2/27/2017.
 */

public class ShoppingList {
    String listName;
    String owner;
    private HashMap<String, Object> dateLastChanged;
    private HashMap<String, Object> timestampCreated;
    private String key;
    private HashMap<String, User> usersShopping;

    public ShoppingList() {
    }

    public ShoppingList(String listName, String owner, String key) {
        this.listName = listName;
        this.owner = owner;
        this.key = key;

        HashMap<String, Object> timestampCreatedObj = new HashMap<String, Object>();
        timestampCreatedObj.put("date", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampCreatedObj;

        HashMap<String, Object> dateLastChangedObj = new HashMap<String, Object>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
        this.dateLastChanged = dateLastChangedObj;
        this.usersShopping = new HashMap<>();

    }

    public String getListName() {
        return listName;
    }
    public String getKey() { return key; }
    public String getOwner() {
        return owner;
    }
    public HashMap<String, Object> getDateLastChanged() {
        return dateLastChanged;
    }
    public HashMap<String, Object> getTimestampCreated() { return timestampCreated; }
    public HashMap getUsersShopping() {
        return usersShopping;
    }

    @Exclude
    public long getDateLastChangedLong() {
        return (long)dateLastChanged.get("date");
    }

    @Exclude
    public long getTimestampCreatedLong() {
        return (long)timestampCreated.get("date");
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("listName", listName);
        result.put("owner", owner);
        result.put("key", key);
        result.put("dateLastChanged", dateLastChanged);
        result.put("timestampCreated", timestampCreated);
        return result;
    }
}
