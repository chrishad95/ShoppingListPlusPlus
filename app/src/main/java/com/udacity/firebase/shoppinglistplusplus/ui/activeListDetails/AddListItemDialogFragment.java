package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingListItem;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Lets user add new list item.
 */
public class AddListItemDialogFragment extends EditListDialogFragment {

    private String mListKey;
    private String mUserEmail;


    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static AddListItemDialogFragment newInstance(ShoppingList shoppingList, String userEmail) {
        AddListItemDialogFragment addListItemDialogFragment = new AddListItemDialogFragment();

        Bundle bundle = newInstanceHelper(shoppingList, R.layout.dialog_add_item);

        bundle.putString(Constants.EXTRA_LIST_KEY, shoppingList.getKey());
        bundle.putString(Constants.EXTRA_USER_NAME, userEmail);

        addListItemDialogFragment.setArguments(bundle);

        return addListItemDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListKey = getArguments().getString(Constants.EXTRA_LIST_KEY);
        mUserEmail = getArguments().getString(Constants.EXTRA_USER_NAME);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** {@link EditListDialogFragment#createDialogHelper(int)} is a
         * superclass method that creates the dialog
         **/
        return super.createDialogHelper(R.string.positive_button_add_list_item);
    }

    /**
     * Adds new item to the current shopping list
     */
    @Override
    protected void doListEdit() {
        String itemName = mEditTextForList.getText().toString();
        if (!itemName.equals("") ) {
            DatabaseReference mFirebaseListsReference = FirebaseDatabase.getInstance().getReference();

            // add item to the list in the shoppingListItems node

            String itemId = mFirebaseListsReference.child(Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS).child(mListKey).push().getKey();
            ShoppingListItem mItem = new ShoppingListItem(itemName, mUserEmail);

            HashMap<String, Object> dateLastChangedObj = new HashMap<String, Object>();
            dateLastChangedObj.put("date", ServerValue.TIMESTAMP);

            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("/" + Constants.FIREBASE_LOCATION_USER_LISTS + "/" + Utils.encodeEmail(mUserEmail) + "/" + mListKey + "/" + Constants.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED + "/", dateLastChangedObj);
            childUpdates.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListKey + "/" + itemId + "/", mItem);

            mFirebaseListsReference.updateChildren(childUpdates);
        }

    }
}
