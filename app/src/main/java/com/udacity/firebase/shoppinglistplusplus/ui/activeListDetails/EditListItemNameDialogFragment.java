package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Lets user edit list item name for all copies of the current list
 */
public class EditListItemNameDialogFragment extends EditListDialogFragment {

    private String mListKey;
    private String mItemKey;
    private String mItemName;
    private String mListOwner;


    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static EditListItemNameDialogFragment newInstance(ShoppingList shoppingList, String itemName, String itemKey, String listKey) {
        EditListItemNameDialogFragment editListItemNameDialogFragment = new EditListItemNameDialogFragment();

        Bundle bundle = EditListDialogFragment.newInstanceHelper(shoppingList, R.layout.dialog_edit_item);

        bundle.putString(Constants.EXTRA_LIST_KEY, listKey);
        bundle.putString(Constants.EXTRA_ITEM_KEY, itemKey);
        bundle.putString(Constants.EXTRA_ITEM_NAME, itemName);
        bundle.putString(Constants.EXTRA_USER_NAME, shoppingList.getOwner());

        editListItemNameDialogFragment.setArguments(bundle);

        return editListItemNameDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListKey = getArguments().getString(Constants.EXTRA_LIST_KEY);
        mItemKey = getArguments().getString(Constants.EXTRA_ITEM_KEY);
        mItemName = getArguments().getString(Constants.EXTRA_ITEM_NAME);
        mListOwner = getArguments().getString(Constants.EXTRA_USER_NAME);


    }


    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** {@link EditListDialogFragment#createDialogHelper(int)} is a
         * superclass method that creates the dialog
         */
        Dialog dialog = super.createDialogHelper(R.string.positive_button_edit_item);
        return dialog;
    }

    /**
     * Change selected list item name to the editText input if it is not empty
     */
    protected void doListEdit() {
        String itemName = mEditTextForList.getText().toString();
        if (!itemName.equals("")) {
            if (!itemName.equals(mItemName)) {
                // update the item name and update the list changed timestamp
                HashMap<String, Object> updateValues = new HashMap<>();

                Utils.updateMapWithTimestampLastChanged(mListKey,mListOwner,updateValues);
                updateValues.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListOwner + "/" + mListKey + "/" + mItemKey + "/" + Constants.FIREBASE_PROPERTY_ITEM_NAME + "/", itemName);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateValues);
            }
        }

    }
}
