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

import java.util.HashMap;

/**
 * Lets user add new list item.
 */
public class AddListItemDialogFragment extends EditListDialogFragment {

    private String mListKey;

    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static AddListItemDialogFragment newInstance(ShoppingList shoppingList) {
        AddListItemDialogFragment addListItemDialogFragment = new AddListItemDialogFragment();

        Bundle bundle = newInstanceHelper(shoppingList, R.layout.dialog_add_item);

        bundle.putString(Constants.EXTRA_LIST_KEY, shoppingList.getKey());
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

            String itemId = mFirebaseListsReference.child("shoppingListItems").child(mListKey).push().getKey();
            ShoppingListItem mItem = new ShoppingListItem(itemName, "anonymous");

            mFirebaseListsReference.child("shoppingListItems").child(mListKey).child(itemId).setValue(mItem);

            HashMap<String, Object> dateLastChangedObj = new HashMap<String, Object>();
            dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
            mFirebaseListsReference.child("lists").child(mListKey).child(Constants.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED).setValue(dateLastChangedObj);

        }

    }
}
