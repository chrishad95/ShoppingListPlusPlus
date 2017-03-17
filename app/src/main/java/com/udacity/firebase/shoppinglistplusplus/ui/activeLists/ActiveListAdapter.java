package com.udacity.firebase.shoppinglistplusplus.ui.activeLists;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
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

import org.w3c.dom.Text;

/**
 * Created by New Owner on 3/2/2017.
 */

public class ActiveListAdapter extends FirebaseListAdapter<ShoppingList> {
    private String mUserEmail;

    public ActiveListAdapter(Activity activity, Class<ShoppingList> modelClass, int modelLayout, Query ref, String userEmail) {
        super(activity, modelClass, modelLayout, ref);
        this.mUserEmail = userEmail;
        this.mActivity = activity;
    }

    @Override
    protected void populateView(View v, ShoppingList model, int position) {
        TextView textViewListName = (TextView) v.findViewById(R.id.text_view_list_name);
        final TextView textViewCreatedByUser = (TextView) v.findViewById(R.id.text_view_created_by_user);
        TextView textViewUsersShopping = (TextView) v.findViewById(R.id.text_view_people_shopping_count);

        String ownerEmail = model.getOwner();

        if (ownerEmail != null) {
            if (ownerEmail.equals(mUserEmail)) {
                textViewCreatedByUser.setText(mActivity.getResources().getString(R.string.text_you));
            } else {
                FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS)
                        .child(Utils.encodeEmail(ownerEmail)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User ownerUser = dataSnapshot.getValue(User.class);
                                if (ownerUser != null) {
                                    textViewCreatedByUser.setText(ownerUser.getName());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }
        textViewListName.setText(model.getListName());
        //textViewCreatedByUser.setText(model.getOwner());

        int numberOfPeopleShopping = 0;
        if (model.getUsersShopping() != null) {
            numberOfPeopleShopping = model.getUsersShopping().size();
        }

        switch (numberOfPeopleShopping) {
            case 0:
                textViewUsersShopping.setText("");
                break;
            case 1:
                textViewUsersShopping.setText(model.getUsersShopping().size() + "person shopping");
                break;
            default:
                textViewUsersShopping.setText(model.getUsersShopping().size() + "people shopping");
        }

    }
}
