package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingListItem;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.sharing.ShareListActivity;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents the details screen for the selected shopping list
 */
public class ActiveListDetailsActivity extends BaseActivity {
    private static final String LOG_TAG = ActiveListDetailsActivity.class.getSimpleName();


    private ListView mListView;
    private Button mToggleShoppingModeButton;
    private TextView mUsersShoppingTextView;


    private ShoppingList mShoppingList;
    private String mListKey;
    private boolean mCurrentUserIsOwner = false;
    private boolean mShopping = false;
    private User mCurrentUser;

    private DatabaseReference mShoppingListReference;
    private DatabaseReference mShoppingListItemsReference;
    private ActiveListItemAdapter mShoppingListItemsAdapter;
    private DatabaseReference mCurrentUserReference;

    private ValueEventListener mShoppingListListener;
    private ValueEventListener mCurrentUserListener;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list_details);

        mListKey = getIntent().getStringExtra(Constants.EXTRA_LIST_KEY);
        if (mListKey == null) {
            finish();
            return;
        }

        // create firebase references
        mShoppingListReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USER_LISTS).child(Utils.encodeEmail(mUserEmail)).child(mListKey);
        mShoppingListItemsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS).child(mListKey);
        mCurrentUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS).child(Utils.encodeEmail(mUserEmail));

        Query sortedItemsRef = mShoppingListItemsReference.orderByChild(Constants.FIREBASE_PROPERTY_ITEM_BOUGHT);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        // set up the list view to show items in the shopping list items adapter
        mShoppingListItemsAdapter = new ActiveListItemAdapter(this, ShoppingListItem.class, R.layout.single_active_list_item, sortedItemsRef, mListKey, mUserEmail);
        mListView.setAdapter(mShoppingListItemsAdapter);

        ValueEventListener shoppingListListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                if (shoppingList == null) {
                    finish();
                    return;

                }
                mShoppingList = shoppingList;

                mShoppingListItemsAdapter.setShoppingList(mShoppingList);
                mCurrentUserIsOwner = Utils.checkIfOwner(mShoppingList, mUserEmail);

                 /* Calling invalidateOptionsMenu causes onCreateOptionsMenu to be called */
                invalidateOptionsMenu();
                setTitle(shoppingList.getListName());

                HashMap<String, User> usersShopping = mShoppingList.getUsersShopping();

                if (usersShopping != null && usersShopping.size() != 0 && usersShopping.containsKey(Utils.encodeEmail(mUserEmail)) ) {
                    mShopping = true;
                    mToggleShoppingModeButton.setText(getString(R.string.button_stop_shopping));
                    mToggleShoppingModeButton.setBackgroundColor(ContextCompat.getColor(ActiveListDetailsActivity.this, R.color.dark_grey));
                } else {
                    mToggleShoppingModeButton.setText(getString(R.string.button_start_shopping));
                    mToggleShoppingModeButton.setBackgroundColor(ContextCompat.getColor(ActiveListDetailsActivity.this, R.color.primary_dark));
                    mShopping = false;
                }


                setWhosShoppingText(mShoppingList.getUsersShopping());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActiveListDetailsActivity.this, "Failed to load list." , Toast.LENGTH_SHORT).show();
            }
        };
        mShoppingListReference.addValueEventListener(shoppingListListener);

        // keep a copy so of listener so we can remove it when app stops
        mShoppingListListener = shoppingListListener;

        mCurrentUserListener = mCurrentUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    mCurrentUser = currentUser;
                } else {
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG, getString(R.string.log_error_the_read_failed) + databaseError.getMessage());
            }
        });


        /**
         * Set up click listeners for interaction.
         */

        /* Show edit list item name dialog on listView item long click event */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /* Check that the view is not the empty footer item */
                if(view.getId() != R.id.list_view_footer_empty) {
                    ShoppingListItem shoppingListItem = mShoppingListItemsAdapter.getItem(position);
                    // only the owner of the list or the owner of the item
                    // can edit the item name if they are not shopping and the item is not bought

                    if (shoppingListItem != null) {
                        String itemOwner = shoppingListItem.getItemOwner();
                        if ((itemOwner.equals(mCurrentUser.getEmail()) || itemOwner.equals(mShoppingList.getOwner()))
                                && !mShopping
                                && !shoppingListItem.isItemBought()  ) {
                            String itemName = shoppingListItem.getItemName();
                            String itemKey = mShoppingListItemsAdapter.getRef(position).getKey();
                            showEditListItemNameDialog(itemName, itemKey);
                        }

                    }
                }
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getId() != R.id.list_view_footer_empty) {
                    String itemKey = mShoppingListItemsAdapter.getRef(position).getKey();
                    // only the user who bought the item can unBuy the item
                    if (mShopping) {
                        Map<String, Object> childUpdates = new HashMap<>();

                        // add item to the list in the shoppingListItems node
                        ShoppingListItem shoppingListItem = mShoppingListItemsAdapter.getItem(position);
                        if (shoppingListItem.isItemBought()) {
                            if (shoppingListItem.getItemBoughtBy().equals(mUserEmail)) {
                                childUpdates.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListKey + "/" + itemKey + "/" + Constants.FIREBASE_PROPERTY_ITEM_BOUGHT_BY + "/", null);
                                childUpdates.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListKey + "/" + itemKey + "/" + Constants.FIREBASE_PROPERTY_ITEM_BOUGHT + "/", false);
                            }
                        } else {
                            childUpdates.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListKey + "/" + itemKey + "/" + Constants.FIREBASE_PROPERTY_ITEM_BOUGHT_BY + "/", mUserEmail);
                            childUpdates.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListKey + "/" + itemKey + "/" + Constants.FIREBASE_PROPERTY_ITEM_BOUGHT + "/", true);
                        }
                        if (childUpdates.size() > 0) {
                            FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
                        }
                    }
                }

            }
        });


        mToggleShoppingModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShopping(v);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_list_details, menu);

        /**
         * Get menu items
         */
        MenuItem remove = menu.findItem(R.id.action_remove_list);
        MenuItem edit = menu.findItem(R.id.action_edit_list_name);
        MenuItem share = menu.findItem(R.id.action_share_list);
        MenuItem archive = menu.findItem(R.id.action_archive);

        /* Only the edit and remove options are implemented */
        remove.setVisible(mCurrentUserIsOwner);
        edit.setVisible(mCurrentUserIsOwner);
        share.setVisible(true);
        archive.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /**
         * Show edit list dialog when the edit action is selected
         */
        if (id == R.id.action_edit_list_name) {
            showEditListNameDialog();
            return true;
        }

        /**
         * removeList() when the remove action is selected
         */
        if (id == R.id.action_remove_list) {
            removeList();
            return true;
        }

        /**
         * Eventually we'll add this
         */
        if (id == R.id.action_share_list) {
            if (mCurrentUserIsOwner) {
                Intent intent = new Intent(ActiveListDetailsActivity.this, ShareListActivity.class);
                intent.putExtra(Constants.EXTRA_USER_NAME, mUserEmail);
                intent.putExtra(Constants.EXTRA_LIST_KEY, mListKey);
                startActivity(intent);
            }
            return true;
        }

        /**
         * archiveList() when the archive action is selected
         */
        if (id == R.id.action_archive) {
            archiveList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Cleanup when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mShoppingListListener != null) {
            mShoppingListReference.removeEventListener(mShoppingListListener);
        }
        if (mShoppingListItemsAdapter != null) {
            mShoppingListItemsAdapter.cleanup();
        }
        if (mCurrentUserListener != null) {
            mCurrentUserReference.removeEventListener(mCurrentUserListener);
        }

    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    private void initializeScreen() {
        mListView = (ListView) findViewById(R.id.list_view_shopping_list_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        /* Common toolbar setup */
        setSupportActionBar(toolbar);
        /* Add back button to the action bar */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /* Inflate the footer, set root layout to null*/
        View footer = getLayoutInflater().inflate(R.layout.footer_empty, null);
        mListView.addFooterView(footer);


        mToggleShoppingModeButton = (Button) findViewById(R.id.button_shopping);
        mUsersShoppingTextView = (TextView) findViewById(R.id.text_view_people_shopping);

    }


    /**
     * Archive current list when user selects "Archive" menu item
     */
    public void archiveList() {
    }


    /**
     * Start AddItemsFromMealActivity to add meal ingredients into the shopping list
     * when the user taps on "add meal" fab
     */
    public void addMeal(View view) {
    }

    /**
     * Remove current shopping list and its items from all nodes
     */
    public void removeList() {
        /* Create an instance of the dialog fragment and show it */
        DialogFragment dialog = RemoveListDialogFragment.newInstance(mShoppingList);

        dialog.show(getFragmentManager(), "RemoveListDialogFragment");
    }

    /**
     * Show the add list item dialog when user taps "Add list item" fab
     */
    public void showAddListItemDialog(View view) {
        /* Create an instance of the dialog fragment and show it */
        DialogFragment dialog = AddListItemDialogFragment.newInstance(mShoppingList, mUserEmail);
        dialog.show(getFragmentManager(), "AddListItemDialogFragment");
    }

    /**
     * Show edit list name dialog when user selects "Edit list name" menu item
     */
    public void showEditListNameDialog() {
        /* Create an instance of the dialog fragment and show it */
        DialogFragment dialog = EditListNameDialogFragment.newInstance(mShoppingList);
        dialog.show(this.getFragmentManager(), "EditListNameDialogFragment");
    }

    /**
     * Show the edit list item name dialog after longClick on the particular item
     */
    public void showEditListItemNameDialog(String itemName, String itemKey) {
        /* Create an instance of the dialog fragment and show it */
        DialogFragment dialog = EditListItemNameDialogFragment.newInstance(mShoppingList, itemName, itemKey, mListKey);
        dialog.show(this.getFragmentManager(), "EditListItemNameDialogFragment");
    }

    /**
     * This method is called when user taps "Start/Stop shopping" button
     */
    public void toggleShopping(View view) {

        DatabaseReference usersShoppingRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> updatedDataMap = new HashMap<String, Object>();
        String propertyToUpdate = Constants.FIREBASE_PROPERTY_USERS_SHOPPING + "/" + Utils.encodeEmail(mUserEmail);

        if (mShopping) {
            Utils.updateMapForAllWithValue(mListKey,mUserEmail,updatedDataMap, propertyToUpdate, null);
            Utils.updateMapWithTimestampLastChanged(mListKey,mUserEmail,updatedDataMap);
        } else {
            Utils.updateMapForAllWithValue(mListKey,mUserEmail,updatedDataMap,propertyToUpdate, mCurrentUser.toMap());
            Utils.updateMapWithTimestampLastChanged(mListKey,mUserEmail,updatedDataMap);
        }
        usersShoppingRef.updateChildren(updatedDataMap);
    }


    /**
     * Set appropriate text for Start/Stop shopping button and Who's shopping textView
     * depending on the current user shopping status
     */
    private void setWhosShoppingText(HashMap<String, User> usersShopping) {

        if (usersShopping != null) {
            ArrayList<String> usersWhoAreNotYou = new ArrayList<>();
            /**
             * If at least one user is shopping
             * Add userName to the list of users shopping if this user is not current user
             */
            for (User user : usersShopping.values()) {
                if (user != null && !(user.getEmail().equals(mUserEmail))) {
                    usersWhoAreNotYou.add(user.getName());
                }
            }

            int numberOfUsersShopping = usersShopping.size();
            String usersShoppingText;

            /**
             * If current user is shopping...
             * If current user is the only person shopping, set text to "You are shopping"
             * If current user and one user are shopping, set text "You and userName are shopping"
             * Else set text "You and N others shopping"
             */
            if (mShopping) {
                switch (numberOfUsersShopping) {
                    case 1:
                        usersShoppingText = getString(R.string.text_you_are_shopping);
                        break;
                    case 2:
                        usersShoppingText = String.format(
                                getString(R.string.text_you_and_other_are_shopping),
                                usersWhoAreNotYou.get(0));
                        break;
                    default:
                        usersShoppingText = String.format(
                                getString(R.string.text_you_and_number_are_shopping),
                                usersWhoAreNotYou.size());
                }
                /**
                 * If current user is not shopping..
                 * If there is only one person shopping, set text to "userName is shopping"
                 * If there are two users shopping, set text "userName1 and userName2 are shopping"
                 * Else set text "userName and N others shopping"
                 */
            } else {
                switch (numberOfUsersShopping) {
                    case 1:
                        usersShoppingText = String.format(
                                getString(R.string.text_other_is_shopping),
                                usersWhoAreNotYou.get(0));
                        break;
                    case 2:
                        usersShoppingText = String.format(
                                getString(R.string.text_other_and_other_are_shopping),
                                usersWhoAreNotYou.get(0),
                                usersWhoAreNotYou.get(1));
                        break;
                    default:
                        usersShoppingText = String.format(
                                getString(R.string.text_other_and_number_are_shopping),
                                usersWhoAreNotYou.get(0),
                                usersWhoAreNotYou.size() - 1);
                }
            }
            mUsersShoppingTextView.setText(usersShoppingText);
        } else {
            mUsersShoppingTextView.setText("");
        }
    }
}
