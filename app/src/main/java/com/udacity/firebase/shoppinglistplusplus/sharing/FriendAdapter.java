package com.udacity.firebase.shoppinglistplusplus.sharing;

import android.app.Activity;
import android.util.Log;
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
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;
import java.util.Objects;

/**
 * Populates the list_view_friends_share inside ShareListActivity
 */
public class FriendAdapter extends FirebaseListAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    //private HashMap <Firebase, ValueEventListener> mLocationListenerMap;
    private String mListKey;
    private HashMap<String, User> mSharedUsersList;
    private ShoppingList mShoppingList;
    private Activity mActivity;

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public FriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                         Query ref, String listKey) {
        super(activity.getApplicationContext(), modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListKey = listKey;

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
        this.mShoppingList = shoppingList;
        this.notifyDataSetChanged();

    }

    /**
     * Public method that is used to pass SharedUsers when they are loaded in ValueEventListener
     */
    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {
        this.mSharedUsersList = sharedUsersList;
        this.notifyDataSetChanged();
    }


    /**
     * This method does the tricky job of adding or removing a friend from the sharedWith list.
     * @param addFriend This is true if the friend is being added, false is the friend is being removed.
     * @param friendToAddOrRemove This is the friend to either add or remove
     * @return
     */
    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend, User friendToAddOrRemove) {
        HashMap<String, Object> updatedUserData = new HashMap<String, Object>();

        if (addFriend) {
            final HashMap<String, Object> friendForFirebase = (HashMap<String, Object>) friendToAddOrRemove.toMap();
            updatedUserData.put(Constants.FIREBASE_LOCATION_SHARED_WITH + "/" + mListKey + "/" + Utils.encodeEmail(friendToAddOrRemove.getEmail()), friendForFirebase);
        } else {
            updatedUserData.put(Constants.FIREBASE_LOCATION_SHARED_WITH + "/" + mListKey +"/" + Utils.encodeEmail(friendToAddOrRemove.getEmail()), null);
        }
        Utils.updateMapWithTimestampLastChanged(mListKey, mShoppingList.getOwner(), updatedUserData);
        return updatedUserData;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    protected void populateView(View v, User model, final int position) {
        TextView textViewUserName = (TextView) v.findViewById(R.id.user_name);

        final ImageButton imageButtonToggleShare = (ImageButton) v.findViewById(R.id.button_toggle_share);


        final DatabaseReference sharedFriendInShoppingListRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_SHARED_WITH)
                .child(mListKey)
                .child(Utils.encodeEmail(getItem(position).getEmail()));

        ValueEventListener listener = sharedFriendInShoppingListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User sharedFriendInShoppingList = dataSnapshot.getValue(User.class);
                if (sharedFriendInShoppingList != null) {
                    imageButtonToggleShare.setImageResource(R.drawable.ic_shared_check);
                    imageButtonToggleShare.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            HashMap<String, Object> updatedUserData = updateFriendInSharedWith(false, getItem(position));

                            FirebaseDatabase.getInstance().getReference().updateChildren(updatedUserData);
                        }
                    });
                } else {
                    imageButtonToggleShare.setImageResource(R.drawable.icon_add_friend);
                    imageButtonToggleShare.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            HashMap<String, Object> updatedUserData = updateFriendInSharedWith(true, getItem(position));
                            FirebaseDatabase.getInstance().getReference().updateChildren(updatedUserData);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

        textViewUserName.setText(getItem(position).getName());
    }

    public void toggleShare(final User user) {
        final DatabaseReference sharedUserReference = FirebaseDatabase.getInstance()
                .getReference().child(Constants.FIREBASE_LOCATION_SHARED_WITH)
                .child(mListKey)
                .child(Utils.encodeEmail(user.getEmail()));
        final HashMap<String, Object> userMap = (HashMap) user.toMap();

        sharedUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User sharedWithUser = dataSnapshot.getValue(User.class);
                if (sharedWithUser != null) {
                    sharedUserReference.setValue(null);
                } else {
                    HashMap childUpdates = new HashMap();
                    childUpdates.put(Constants.FIREBASE_LOCATION_SHARED_WITH + "/" + mListKey + '/' + Utils.encodeEmail(user.getEmail()),user);
                    sharedUserReference.setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
