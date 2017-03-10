package com.udacity.firebase.shoppinglistplusplus.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by New Owner on 3/10/2017.
 */

public class User {
    private String name;
    private String email;
    private HashMap<String, Object> timestampJoined;

    public User () {

    }

    public User (String name, String email) {
        this.name = name;
        this.email = email;

        HashMap<String, Object> timestampObj = new HashMap<String, Object>();
        timestampObj.put("date", ServerValue.TIMESTAMP);
        this.timestampJoined = timestampObj;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    @Exclude
    public long getTimestampCreatedLong() {
        return (long)timestampJoined.get("date");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.FIREBASE_PROPERTY_USER_NAME, name);
        result.put(Constants.FIREBASE_PROPERTY_USER_EMAIL, email);
        result.put(Constants.FIREBASE_PROPERTY_TIMESTAMP_JOINED, timestampJoined);
        return result;
    }
}
