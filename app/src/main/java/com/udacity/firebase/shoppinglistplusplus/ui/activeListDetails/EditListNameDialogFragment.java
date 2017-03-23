package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

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
 * Lets user edit the list name for all copies of the current list
 */
public class EditListNameDialogFragment extends EditListDialogFragment {
    private static final String LOG_TAG = ActiveListDetailsActivity.class.getSimpleName();
    String mListName;
    String mListKey;
    String mOwnerEmail;


    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static EditListNameDialogFragment newInstance(ShoppingList shoppingList) {
        EditListNameDialogFragment editListNameDialogFragment = new EditListNameDialogFragment();
        //editListNameDialogFragment.helpSetDefaultValueEditText(shoppingList.getListName());

        Bundle bundle = EditListDialogFragment.newInstanceHelper(shoppingList, R.layout.dialog_edit_list);
        bundle.putString(Constants.EXTRA_LIST_NAME, shoppingList.getListName());
        bundle.putString(Constants.EXTRA_LIST_KEY, shoppingList.getKey());
        bundle.putString(Constants.EXTRA_USER_NAME, shoppingList.getOwner());

        editListNameDialogFragment.setArguments(bundle);
        return editListNameDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListName = getArguments().getString(Constants.EXTRA_LIST_NAME);
        mListKey = getArguments().getString(Constants.EXTRA_LIST_KEY);
        mOwnerEmail = getArguments().getString(Constants.EXTRA_USER_NAME);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** {@link EditListDialogFragment#createDialogHelper(int)} is a
         * superclass method that creates the dialog
         **/
        Dialog dialog = super.createDialogHelper(R.string.positive_button_edit_item);
        helpSetDefaultValueEditText(mListName);

        return dialog;
    }

    /**
     * Changes the list name in all copies of the current list
     */
    protected void doListEdit() {

        final String inputListName = mEditTextForList.getText().toString();
        if (!inputListName.equals("")) {
            if (!inputListName.equals(mListName)) {

                DatabaseReference mFirebaseReference = FirebaseDatabase.getInstance().getReference();
                HashMap<String, Object> updatedProperties = new HashMap<String, Object>();

                //updatedProperties.put(Constants.FIREBASE_PROPERTY_LIST_NAME, inputListName);

                Utils.updateMapForAllWithValue(mListKey,mOwnerEmail,updatedProperties,Constants.FIREBASE_PROPERTY_LIST_NAME,inputListName);
                Utils.updateMapWithTimestampLastChanged(mListKey,mOwnerEmail,updatedProperties);

                mFirebaseReference.updateChildren(updatedProperties);
            }
        }

    }
}

