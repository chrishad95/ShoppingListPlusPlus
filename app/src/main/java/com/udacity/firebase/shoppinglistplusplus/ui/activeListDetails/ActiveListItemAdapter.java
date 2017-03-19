package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingListItem;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;

import java.util.HashMap;

/**
 * Populates list_view_shopping_list_items inside ActiveListDetailsActivity
 */
public class ActiveListItemAdapter extends FirebaseListAdapter<ShoppingListItem> {
    private String mListKey;
    private ShoppingList mShoppingList;
    private String mUserEmail;

    public void setShoppingList(ShoppingList shoppingList) {
        mShoppingList = shoppingList;
        this.notifyDataSetChanged();
    }
    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public ActiveListItemAdapter(Activity activity, Class<ShoppingListItem> modelClass, int modelLayout,
                                 Query ref, String listKey, String userEmail) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListKey = listKey;
        this.mUserEmail = userEmail;
    }
    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_active_list_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, final ShoppingListItem item, int position) {
        // TODO Set up the view so that it shows the name of the item and the trash can button.

        TextView textViewItemName = (TextView) view.findViewById(R.id.text_view_active_list_item_name);
        ImageButton buttonRemoveItem = (ImageButton) view.findViewById(R.id.button_remove_item);
        TextView textViewBoughtBy = (TextView) view.findViewById(R.id.text_view_bought_by);
        TextView textViewBoughtByUser = (TextView) view.findViewById(R.id.text_view_bought_by_user);

        textViewItemName.setText(item.getItemName());

        if (item.isItemBought()) {
            textViewItemName.setPaintFlags(textViewItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            buttonRemoveItem.setEnabled(false);
            buttonRemoveItem.setVisibility(ImageButton.INVISIBLE);

            textViewBoughtBy.setText("Bought by ");
            textViewBoughtByUser.setText(item.getItemBoughtBy());

            if (item.getItemBoughtBy().equals(mUserEmail)) {
                textViewBoughtByUser.setText(R.string.text_you);
            }
            textViewBoughtBy.setVisibility(TextView.VISIBLE);
            textViewBoughtByUser.setVisibility(TextView.VISIBLE);
        } else {
            textViewItemName.setPaintFlags(textViewItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            textViewBoughtBy.setText("");
            textViewBoughtByUser.setText("");

            textViewBoughtBy.setVisibility(TextView.INVISIBLE);
            textViewBoughtByUser.setVisibility(TextView.INVISIBLE);

            // remove item button should only be visible if you are the owner of the list
            // or if you are the owner of the item
            String itemOwner = item.getItemOwner();
            if (itemOwner.equals(mUserEmail) || (mShoppingList != null && mShoppingList.getOwner().equals(mUserEmail))) {
                buttonRemoveItem.setVisibility(ImageButton.VISIBLE);
            } else {
                buttonRemoveItem.setVisibility(ImageButton.INVISIBLE);
            }
            buttonRemoveItem.setEnabled(true);
        }
        final String itemToRemoveKey = this.getRef(position).getKey();

        buttonRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //The trashcan button should trigger a dialog to appear.
                // The code to make the dialog appear is commented out below.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity, R.style.CustomTheme_Dialog)
                        .setTitle(mActivity.getString(R.string.remove_item_option))
                        .setMessage(mActivity.getString(R.string.dialog_message_are_you_sure_remove_item))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                removeItem(itemToRemoveKey);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                        /* Dismiss the dialog */
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


    }

    private void removeItem(String itemKey) {
        // if we remove and item from the list, we need to remove the item and update the list changed timestamp

        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"  + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListKey + "/" + itemKey, null);


        HashMap<String, Object> dateLastChangedObj = new HashMap<String, Object>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);

        childUpdates.put("/" + Constants.FIREBASE_LOCATION_ACTIVE_LISTS + "/" + mListKey + "/" + Constants.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED, dateLastChangedObj);

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);



    }
}