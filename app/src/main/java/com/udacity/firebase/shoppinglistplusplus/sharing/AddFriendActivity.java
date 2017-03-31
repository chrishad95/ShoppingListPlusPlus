package com.udacity.firebase.shoppinglistplusplus.sharing;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;

/**
 * Represents the Add Friend screen and functionality
 */
public class AddFriendActivity extends BaseActivity {
    private EditText mEditTextAddFriendEmail;
    private ListView mListViewAutocomplete;

    private AutocompleteFriendAdapter mAutocompleteFriendAdapter;
    private DatabaseReference mUsersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        mUsersReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS);
        mAutocompleteFriendAdapter = new AutocompleteFriendAdapter(this, User.class, R.layout.single_autocomplete_item, mUsersReference, mUserEmail);
        mListViewAutocomplete.setAdapter(mAutocompleteFriendAdapter);
        /**
         * Set interactive bits, such as click events/adapters
         */
        /**mEditTextAddFriendEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });**/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAutocompleteFriendAdapter != null) {
            mAutocompleteFriendAdapter.cleanup();
        }
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        mListViewAutocomplete = (ListView) findViewById(R.id.list_view_friends_autocomplete);
        mEditTextAddFriendEmail = (EditText) findViewById(R.id.edit_text_add_friend_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        /* Add back button to the action bar */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
