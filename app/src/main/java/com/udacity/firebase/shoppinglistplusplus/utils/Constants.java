package com.udacity.firebase.shoppinglistplusplus.utils;

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {

    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where active lists are stored (ie "activeLists")
     */
    public static final String FIREBASE_LOCATION_SHOPPING_LIST_ITEMS = "/shoppingListItems/";
    public static final String FIREBASE_LOCATION_ACTIVE_LISTS = "/lists/";


    /**
     * Constants for Firebase object properties
     */


    /**
     * Constants for Firebase URL
     */



    /**
     * Constants for bundles, extras and shared preferences keys
     */
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";

    public static final String EXTRA_LIST_NAME = "LIST_NAME";
    public static final String EXTRA_LIST_KEY = "LIST_KEY";
    public static final String EXTRA_ITEM_KEY = "ITEM_KEY";
    public static final String EXTRA_ITEM_NAME = "ITEM_NAME";

    public static final String FIREBASE_PROPERTY_LIST_NAME = "listName";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "dateLastChanged";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_CREATED = "timestampCreated";
    public static final String FIREBASE_PROPERTY_LIST_OWNER = "owner";
    public static final String FIREBASE_PROPERTY_ITEM_NAME = "itemName";
    public static final String FIREBASE_PROPERTY_ITEM_OWNER = "itemOwner";


}
