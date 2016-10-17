package com.example.maheshpujala.sillymonks.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CallbackManager mFacebookCallbackManager;
    private SignInButton mGoogleSignInButton;
    private LoginButton mFacebookSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    Bundle savedInstanceState;
    private String accessToken;
    Button skip;
    boolean skipVisible = false;
    SessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
         setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Session Manager
        session = new SessionManager(getApplicationContext());

        showSkip();

        // Set Up Fb Plugin
        mFacebookSignInButton = (LoginButton) findViewById(R.id.fb_login_button);
        getLoginDetails(mFacebookSignInButton);

//G+ Plugin
        mGoogleSignInButton = (SignInButton) findViewById(R.id.gplus_signin_button);
        setGooglePlusButtonText(mGoogleSignInButton,"Google");
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

            // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_view);

        mPasswordView = (EditText) findViewById(R.id.password_view);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void showSkip() {
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("From Splash");
                if (newString.contains("show_skip")){
                    skip =(Button) findViewById(R.id.skip_button);
                    skip.setVisibility(View.VISIBLE);
                    skipVisible =true;
                    skip.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent main = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(main);
                            finish();
                        }
                    });
                }
            }
        }
    }
    /*
          Initialize the facebook sdk and then callback manager will handle the login responses.
       */
    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(getApplication());

    }

    protected void getLoginDetails(LoginButton mFacebookSignInButton){
        // Callback registration
        mFacebookSignInButton.setReadPermissions(Arrays.asList("email"));
        Log.e("get login details","setReadPermissions");

        mFacebookSignInButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult login_result) {
                        handleSignInResult(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                LoginManager.getInstance().logOut();
                                return null;
                            }
                        });
                    }
                    @Override
                    public void onCancel() {
                        handleSignInResult(null);
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.d(LoginActivity.class.getCanonicalName(), error.getMessage());
                        handleSignInResult(null);
                    }
                }
        );
    }
    protected void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                tv.setGravity(Gravity.CENTER);
                return;
            }
        }
    }
    private void signInWithGoogle() {
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                final GoogleApiClient client = mGoogleApiClient;
                GoogleSignInAccount acct = result.getSignInAccount();

                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                Uri pPhoto = acct.getPhotoUrl();
           //     String gender = acct.getGender();
                String personPhoto =pPhoto.toString();

                if (skipVisible){
                    Intent begin = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(begin);
                    session.createLoginSession(personPhoto,personName, personEmail,"Male","google");
                    finish();
                }else{
                    Intent googleIntent = getIntent();
                    setResult(Activity.RESULT_OK, googleIntent);
                    session.createLoginSession(personPhoto,personName, personEmail,"Male","google");
                    finish();
                }


            } else {
                handleSignInResult(null);
            }
        }  else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }
    private void handleSignInResult(Callable<Void> logout) {
        if (logout == null) {
            /* Login error */
            Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
        } else {
            /* Login success */
            /*
       To get the facebook user's own profile information via  creating a new request.
       When the request is completed, a callback is called to handle the success condition.
    */
            GraphRequest data_request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject json_object,GraphResponse response) {
                            if (response != null) {
                                Bundle bFacebookData = getFacebookData(json_object);
                                Log.e("bfbdata", "" + bFacebookData);
                                String mFbid = bFacebookData.getString("idFacebook");
                                String mFullname = bFacebookData.getString("first_name") + " "+bFacebookData.getString("last_name");
                                String mEmail = bFacebookData.getString("email");
                                String mGender =bFacebookData.getString("gender");
                                Log.e("onCompleted facebook Bundle",""+mFbid+mFullname+mEmail+mGender);

                                if (skipVisible){
                                    Log.e("Skipvisible","Entereed");
                                    Intent begin = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(begin);
                                    session.createLoginSession(mFbid,mFullname, mEmail,mGender,"facebook");
                                    finish();
                                }else{
                                    Intent returnIntent = getIntent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    session.createLoginSession(mFbid,mFullname, mEmail,mGender,"facebook");
                                finish();
                            }
                            }
                            else{
                                Log.e("Response","null");
                            }
                        }
                    });
            Bundle permission_param = new Bundle();
            permission_param.putString("fields", "id, first_name, last_name,gender, email,age_range");
            data_request.setParameters(permission_param);
            data_request.executeAsync();

        }
    }
    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {
            bundle.putString("idFacebook",object.getString("id"));
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

        } catch (JSONException e) {
            Log.e("exp",""+e.getLocalizedMessage());
        }
        return bundle;
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            Log.e("onBackPressed","___________Entered__________");
        }

        return super.onOptionsItemSelected(item);
    }
}

