package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingListItem;

/**
 * Populates list_view_shopping_list_items inside ActiveListDetailsActivity
 */
public class ActiveListItemAdapter extends FirebaseListAdapter<ShoppingListItem> {

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public ActiveListItemAdapter(Activity activity, Class<ShoppingListItem> modelClass, int modelLayout,
                                 Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }
    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_active_list_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, final ShoppingListItem item, int position) {
        // TODO Set up the view so that it shows the name of the item and the trash can button.

        TextView ItemNameTextView = (TextView) view.findViewById(R.id.text_view);

        // The trashcan button should trigger a dialog to appear.
        // The code to make the dialog appear is commented out below.
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity, R.style.CustomTheme_Dialog)
//                .setTitle(mActivity.getString(R.string.remove_item_option))
//                .setMessage(mActivity.getString(R.string.dialog_message_are_you_sure_remove_item))
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        removeItem();
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                                /* Dismiss the dialog */
//                        dialog.dismiss();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert);
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();

    }

    private void removeItem() {

    }
}