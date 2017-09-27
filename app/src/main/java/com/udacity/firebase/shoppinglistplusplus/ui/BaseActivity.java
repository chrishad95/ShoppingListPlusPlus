package com.udacity.firebase.shoppinglistplusplus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.Arrays;

/**
 * BaseActivity class is used as a base class for all activities in the app
 * It implements GoogleApiClient callbacks to enable "Logout" in all activities
 * and defines variables that are being shared across all activities
 */
public abstract class BaseActivity extends AppCompatActivity  {

    protected String mUserEmail;
    private String mUserName;

    public static final int RC_SIGN_IN = 1;

    // Firebase auth variables
    protected FirebaseAuth mFirebaseAuth;
    protected FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        mUserEmail = sp.getString(Constants.KEY_EMAIL, null);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser authUser = firebaseAuth.getCurrentUser();
                if (authUser != null) {
                    // logged in!!!
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor spe = sp.edit();

                    mUserEmail = authUser.getEmail();
                    mUserName = authUser.getDisplayName();

                    spe.putString(Constants.KEY_EMAIL, mUserEmail).apply();

                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS).child(Utils.encodeEmail(mUserEmail))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                // user existed, nothing to do
                            } else {
                                user = new User(mUserName, mUserEmail);
                                FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS).child(Utils.encodeEmail(mUserEmail)).setValue(user);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    private void saveUser() {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // remove the authstate listener
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        if (id == R.id.action_logout) {
            // logout the user

            AuthUI.getInstance().signOut(this);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

    protected void initializeBackground(LinearLayout linearLayout) {

        /**
         * Set different background image for landscape and portrait layouts
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen_land);
        } else {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen);
        }
    }


    private void onSignedOutCleanup() {
        mUserEmail = null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spe = sp.edit();
        spe.clear();
        spe.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        // handle the result from firebaseAuth
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }
    }
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this,"Sign in ok.", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this,"Sign in was cancelled.", Toast.LENGTH_SHORT).show();
        }
    }
}
