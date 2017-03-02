package com.udacity.firebase.shoppinglistplusplus.ui.activeLists;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;

import org.w3c.dom.Text;

/**
 * Created by New Owner on 3/2/2017.
 */

public class ActiveListAdapter extends FirebaseListAdapter<ShoppingList> {

    public ActiveListAdapter(Activity activity, Class<ShoppingList> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    @Override
    protected void populateView(View v, ShoppingList model, int position) {
        TextView textViewListName = (TextView) v.findViewById(R.id.text_view_list_name);
        TextView textViewCreatedByUser = (TextView) v.findViewById(R.id.text_view_created_by_user);

        textViewListName.setText(model.getListName());
        textViewCreatedByUser.setText(model.getOwner());

    }
}
