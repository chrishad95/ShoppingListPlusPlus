package com.udacity.firebase.shoppinglistplusplus.utils;

import android.content.Context;

import com.google.firebase.database.ServerValue;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Utility class
 */
public class Utils {
    /**
     * Format the date with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context mContext = null;


    /**
     * Public constructor that takes mContext for later use
     */
    public Utils(Context con) {
        mContext = con;
    }

    public static String encodeEmail (String email) {
        return email.replace(".", ",");
    }
    public static boolean checkIfOwner(ShoppingList shoppingList, String userEmail) {
        return shoppingList.getOwner().equals(userEmail);
    }

    public static HashMap<String, Object> updateMapForAllWithValue (
            final String listId,
            final String owner,
            HashMap<String, Object> mapToUpdate,
            String propertyToUpdate, Object valueToUpdate) {

        mapToUpdate.put("/" + Constants.FIREBASE_LOCATION_USER_LISTS + "/" + owner + "/"
            + listId + "/" + propertyToUpdate, valueToUpdate);

        return  mapToUpdate;
    }

    public static HashMap<String, Object> updateMapWithTimestampLastChanged (
            final String listId,
            final String owner,
            HashMap<String, Object> mapToAddDateToUpdate) {

        HashMap<String, Object> timestampNowHash = new HashMap<>();
        timestampNowHash.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        updateMapForAllWithValue(listId, owner, mapToAddDateToUpdate, Constants.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED, timestampNowHash);

        return mapToAddDateToUpdate;
    }

}
