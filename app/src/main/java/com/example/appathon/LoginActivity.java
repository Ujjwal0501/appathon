package com.example.appathon;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {


    int RC_SIGN_IN = 1;
    String TAG = "ICICI-LOGIN";
    GoogleSignInClient mGoogleSignInClient;
    CheckBox tc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tc = (CheckBox) findViewById(R.id.tc);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick1(view);
            }
        });

    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Snackbar.make(tc, "You are already signed in.", Snackbar.LENGTH_SHORT).show();
            Intent next = new Intent(LoginActivity.this, AddCard.class);
            LoginActivity.this.startActivity(next);
            this.finish();
        } else
            Snackbar.make(tc, "You are not signed in.", Snackbar.LENGTH_SHORT).show();
        super.onStart();
    }

    public void onClick1(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                if (tc.isChecked())
                    signIn();
                else
                    Snackbar.make(tc, "Please agree to Terms and Conditions.", Snackbar.LENGTH_LONG).show();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {
                Snackbar.make(tc, "Sign-Up successful.", Snackbar.LENGTH_SHORT).show();
                Intent next = new Intent(LoginActivity.this, AddCard.class);
                next.putExtra("account", account.getEmail()+"");
                LoginActivity.this.startActivity(next);
                this.finish();
            } else {
                Snackbar.make(tc, "Error during Sign-Up!", Snackbar.LENGTH_SHORT).show();
            }
            // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Snackbar.make(tc, "Error during Sign-Up!\n"+e, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void tcInfo(View view) {
        Snackbar.make(view, ""+getString(R.string.login_info), Snackbar.LENGTH_LONG).show();
    }
}
