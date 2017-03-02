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

import java.util.HashMap;
import java.util.Map;

/**
 * Lets user edit the list name for all copies of the current list
 */
public class EditListNameDialogFragment extends EditListDialogFragment {
    private static final String LOG_TAG = ActiveListDetailsActivity.class.getSimpleName();
    String mListName;
    String mListKey;


    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static EditListNameDialogFragment newInstance(ShoppingList shoppingList) {
        EditListNameDialogFragment editListNameDialogFragment = new EditListNameDialogFragment();
        //editListNameDialogFragment.helpSetDefaultValueEditText(shoppingList.getListName());

        Bundle bundle = EditListDialogFragment.newInstanceHelper(shoppingList, R.layout.dialog_edit_list);
        bundle.putString(Constants.EXTRA_LIST_NAME, shoppingList.getListName());
        bundle.putString(Constants.EXTRA_LIST_KEY, shoppingList.getKey());

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

                DatabaseReference mFirebaseListsReference = FirebaseDatabase.getInstance().getReference().child("lists").child(mListKey);


                HashMap<String, Object> updatedProperties = new HashMap<String, Object>();

                updatedProperties.put(Constants.FIREBASE_PROPERTY_LIST_NAME, inputListName);

                HashMap<String, Object> dateLastChangedObj = new HashMap<String, Object>();
                dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
                updatedProperties.put(Constants.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED, dateLastChangedObj);

                mFirebaseListsReference.updateChildren(updatedProperties);

            }
        }

    }
}

