package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by New Owner on 2/27/2017.
 */

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {
    public ShoppingListAdapter(Context context, int resource, List<ShoppingList> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.single_active_list, parent, false);
        }
        TextView listNameView = (TextView) convertView.findViewById(R.id.text_view_list_name);
        TextView ownerView = (TextView) convertView.findViewById(R.id.text_view_created_by_user);
        //TextView editTimeView = (TextView) convertView.findViewById(R.id.text_view_edit_time);

        ShoppingList shoppingList = getItem(position);
        listNameView.setText(shoppingList.getListName());
        ownerView.setText(shoppingList.getOwner());
        //editTimeView.setText(Utils.SIMPLE_DATE_FORMAT.format(shoppingList.getDateLastChangedLong()));

        return convertView;
    }
}
