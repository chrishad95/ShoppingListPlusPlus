package com.udacity.firebase.shoppinglistplusplus.sharing;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;

import java.util.HashMap;

/**
 * Populates the list_view_friends_share inside ShareListActivity
 */
public class FriendAdapter extends FirebaseListAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    //private HashMap <Firebase, ValueEventListener> mLocationListenerMap;
    private String mListKey;

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public FriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                         Query ref, String listKey) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListKey = listKey;

        DatabaseReference mSharedWithReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_SHARED_WITH).child(mListKey);
        ChildEventListener mSharedWithListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mSharedWithReference.addChildEventListener(mSharedWithListener);
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_user_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */

    /**
     * Public method that is used to pass ShoppingList object when it is loaded in ValueEventListener
     */
    public void setShoppingList(ShoppingList shoppingList) {

    }

    /**
     * Public method that is used to pass SharedUsers when they are loaded in ValueEventListener
     */
    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {

    }


    /**
     * This method does the tricky job of adding or removing a friend from the sharedWith list.
     * @param addFriend This is true if the friend is being added, false is the friend is being removed.
     * @param friendToAddOrRemove This is the friend to either add or remove
     * @return
     */
    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend, User friendToAddOrRemove) {
        return null;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    protected void populateView(View v, User model, final int position) {
        TextView textViewUserName = (TextView) v.findViewById(R.id.user_name);
        ImageButton imageButtonToggleShare = (ImageButton) v.findViewById(R.id.button_toggle_share);


        textViewUserName.setText(getItem(position).getName());
        imageButtonToggleShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShare(getItem(position));

            }
        });
    }

    public void toggleShare(User user) {

    }
}
