package com.udacity.firebase.shoppinglistplusplus.sharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

/**
 * Allows for you to check and un-check friends that you share the current list with
 */
public class ShareListActivity extends BaseActivity {
    private static final String LOG_TAG = ShareListActivity.class.getSimpleName();
    private ListView mListView;
    private DatabaseReference mFriendsReference;
    private FriendAdapter mFriendAdapter;
    private String mlistKey;
    private String mUserEmail;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);
        mlistKey = getIntent().getStringExtra(Constants.EXTRA_LIST_KEY);
        mUserEmail = getIntent().getStringExtra(Constants.EXTRA_USER_NAME);


        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        mFriendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERFRIENDS).child(Utils.encodeEmail(mUserEmail));
        mFriendAdapter = new FriendAdapter(this, User.class, R.layout.single_user_item, mFriendsReference, mlistKey);

        mListView.setAdapter(mFriendAdapter);
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        mListView = (ListView) findViewById(R.id.list_view_friends_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        /* Add back button to the action bar */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Launch AddFriendActivity to find and add user to current user's friends list
     * when the button AddFriend is pressed
     */
    public void onAddFriendPressed(View view) {
        Intent intent = new Intent(ShareListActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFriendAdapter != null) {
            mFriendAdapter.cleanup();
        }
    }
}
