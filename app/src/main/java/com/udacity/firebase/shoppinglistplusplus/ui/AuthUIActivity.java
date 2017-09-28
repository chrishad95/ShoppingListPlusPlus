package com.udacity.firebase.shoppinglistplusplus.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.udacity.firebase.shoppinglistplusplus.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by New Owner on 9/27/2017.
 */

public class AuthUIActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private Activity mActivity;
    private ViewGroup mRootViewGroup;



    public static Intent createIntent(Context context) {
        return new Intent(context, AuthUIActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mRootViewGroup = (ViewGroup) findViewById(R.id.root);
        setContentView(R.layout.activity_auth_ui);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startSignedInActivity(null);
            finish();
            return;
        }
/*
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                        //, new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                ))
                .build(),
                RC_SIGN_IN);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                startSignedInActivity(response);

                finish();
                return;
            } else {
                if (response == null) {
                    showToast("Sign in was cancelled.");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast("No network");
                    return;

                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showToast("Unknown error code");
                    return;

                }
            }

        }
    }
    private void showToast(String message) {
        Snackbar.make(mRootViewGroup, message, Snackbar.LENGTH_LONG).show();
    }
    private void startSignedInActivity(IdpResponse response) {
        startActivity(MainActivity.createIntent(this, response));
    }
}
