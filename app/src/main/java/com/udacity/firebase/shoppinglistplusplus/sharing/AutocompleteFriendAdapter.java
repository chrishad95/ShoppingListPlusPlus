package com.udacity.firebase.shoppinglistplusplus.sharing;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

/**
 * Populates the list_view_friends_autocomplete inside AddFriendActivity
 */
public class AutocompleteFriendAdapter extends FirebaseListAdapter<User> {
    private String mUseremail;
    private Activity mActivity;


    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public AutocompleteFriendAdapter(Context context, Class<User> modelClass, int modelLayout,
                                     Query ref, String useremail) {
        super(context, modelClass, modelLayout, ref);
        this.mUseremail = useremail;
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_autocomplete_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */

    /** Checks if the friend you try to add is the current user **/
    private boolean isNotCurrentUser(User user) {
        if (user.getEmail().equals(mUseremail)) {
            Toast.makeText(this.mActivity, mContext.getText(R.string.toast_you_cant_add_yourself),Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /** Checks if the friend you try to add is already added, given a dataSnapshot of a user **/
    private boolean isNotAlreadyAdded(DataSnapshot dataSnapshot, User user) {
        if (dataSnapshot.getValue(User.class) != null) {
            String friendError = String.format(mActivity.getResources().getString(R.string.toast_is_already_your_friend), user.getName());
            Toast.makeText(mActivity, friendError, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void populateView(View v, User model, final int position) {

        TextView userEmailTextView = (TextView) v.findViewById(R.id.text_view_autocomplete_item);
        userEmailTextView.setText(model.getEmail());

        userEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotCurrentUser(getItem(position))) {
                    // make sure the user is not already in the friends list for this user
                    // need to get a datasnapshot of the current userfriends for this user
                    final DatabaseReference currentUserFriendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERFRIENDS)
                            .child(Utils.encodeEmail(mUseremail)).child(Utils.encodeEmail(getItem(position).getEmail()));
                    currentUserFriendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (isNotAlreadyAdded(dataSnapshot, getItem(position))) {
                                currentUserFriendsReference.setValue(getItem(position));
                                mActivity.finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(mActivity.getClass().getSimpleName(), mActivity.getString(R.string.log_error_the_read_failed) + databaseError.getMessage());

                        }
                    });

                    // add the user to the friends list
                }
            }
        });

    }
}
