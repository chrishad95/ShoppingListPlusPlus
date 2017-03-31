package com.udacity.firebase.shoppinglistplusplus.utils;

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {

    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where active lists are stored (ie "activeLists")
     */
    public static final String FIREBASE_LOCATION_SHOPPING_LIST_ITEMS = "shopping-list-items";
    public static final String FIREBASE_LOCATION_USER_LISTS = "user-lists";
    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATION_USERFRIENDS = "userFriends";
    public static final String FIREBASE_LOCATION_SHARED_WITH = "sharedWith";



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
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_PREF_SORT_ORDER_LISTS = "key-pref-sort-order-lists";

    public static final String ORDER_BY_KEY = "orderByPushKey";
    public static final String ORDER_BY_OWNER_EMAIL = "orderByOwnerEmail";


    public static final String EXTRA_LIST_NAME = "LIST_NAME";
    public static final String EXTRA_LIST_KEY = "LIST_KEY";
    public static final String EXTRA_ITEM_KEY = "ITEM_KEY";
    public static final String EXTRA_ITEM_NAME = "ITEM_NAME";
    public static final String EXTRA_USER_NAME = "USER_NAME";

    public static final String FIREBASE_PROPERTY_TIMESTAMP = "date";
    public static final String FIREBASE_PROPERTY_LIST_NAME = "listName";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "dateLastChanged";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_CREATED = "timestampCreated";
    public static final String FIREBASE_PROPERTY_LIST_OWNER = "owner";

    public static final String FIREBASE_PROPERTY_ITEM_NAME = "itemName";
    public static final String FIREBASE_PROPERTY_ITEM_OWNER = "itemOwner";
    public static final String FIREBASE_PROPERTY_ITEM_BOUGHT = "itemBought";
    public static final String FIREBASE_PROPERTY_ITEM_BOUGHT_BY = "itemBoughtBy";

    public static final String FIREBASE_PROPERTY_USER_NAME = "name";
    public static final String FIREBASE_PROPERTY_USER_EMAIL = "email";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_JOINED = "timestampJoined";
    public static final String FIREBASE_PROPERTY_USERS_SHOPPING = "usersShopping";


}
