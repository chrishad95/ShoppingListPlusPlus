package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Lets the user remove active shopping list
 */
public class RemoveListDialogFragment extends DialogFragment {
    final static String LOG_TAG = RemoveListDialogFragment.class.getSimpleName();
    private String mListKey;
    private String mListOwner;

    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static RemoveListDialogFragment newInstance(ShoppingList shoppingList) {
        RemoveListDialogFragment removeListDialogFragment = new RemoveListDialogFragment();
        Bundle bundle = new Bundle();

        bundle.putString(Constants.EXTRA_LIST_KEY, shoppingList.getKey());
        bundle.putString(Constants.EXTRA_USER_NAME, shoppingList.getOwner());

        removeListDialogFragment.setArguments(bundle);
        return removeListDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListKey = getArguments().getString(Constants.EXTRA_LIST_KEY);
        mListOwner = getArguments().getString(Constants.EXTRA_USER_NAME);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog)
                .setTitle(getActivity().getResources().getString(R.string.action_remove_list))
                .setMessage(getString(R.string.dialog_message_are_you_sure_remove_list))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeList();
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    private void removeList() {
        HashMap<String, Object> childUpdates = new HashMap<>();

        Utils.updateMapForAllWithValue(mListKey, mListOwner, childUpdates, "", null);

        childUpdates.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListKey, null);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

    }

}
