package com.example.maheshpujala.sillymonks.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.net.Uri;

import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.Callable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maheshpujala.sillymonks.Network.VolleyRequest;
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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements OnClickListener {


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
    TextView sign_up,forgot_password;
    String personName,personEmail,personPhoto,userId,mFbid,mFullname,mEmail,mGender,email,password,fullname,smonksId,first_name,last_name;
    Boolean identity;



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
        sign_up = (TextView) findViewById(R.id.sign_up);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        showSkip();

        // Set Up Fb Plugin
        mFacebookSignInButton = (LoginButton) findViewById(R.id.fb_login_button);
        getLoginDetails(mFacebookSignInButton);

//G+ Plugin
        mGoogleSignInButton = (SignInButton) findViewById(R.id.gplus_signin_button);
        setGooglePlusButtonText(mGoogleSignInButton, "Google");
        mGoogleSignInButton.setOnClickListener(this);

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
        mEmailSignInButton.setOnClickListener(this);
    }

    private void showSkip() {
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("From Splash");
                if (newString.contains("show_skip")) {
                    skip = (Button) findViewById(R.id.skip_button);
                    skip.setVisibility(View.VISIBLE);
                    skipVisible = true;
                    skip.setOnClickListener(this);
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

    protected void getLoginDetails(LoginButton mFacebookSignInButton) {
        // Callback registration
        mFacebookSignInButton.setReadPermissions(Arrays.asList("email"));
        Log.e("get login details", "setReadPermissions");

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
        if (mGoogleApiClient != null) {
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
            if (result.isSuccess()) {
                final GoogleApiClient client = mGoogleApiClient;

                GoogleSignInAccount acct = result.getSignInAccount();
                 personName = acct.getDisplayName();
                 personEmail = acct.getEmail();
                 Uri pPhoto = acct.getPhotoUrl();
                 personPhoto = pPhoto.toString();
                String[] separated = personName.split(" ");
                String firstName= separated[0];
                String lastName= separated[1];
                sendUserDetails(firstName,lastName,personEmail,"google");



            } else {
                handleSignInResult(null);
            }
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void sendUserDetails(String FirstName, String LastName, String UserEmail, final String socialPluginType) {
        String socialRegistration_url =getResources().getString(R.string.main_url)+getResources().getString(R.string.social_registration_url)+getResources().getString(R.string.firstname_url)
                +FirstName+getResources().getString(R.string.lastname_url)+LastName+getResources().getString(R.string.email_url)+UserEmail+getResources().getString(R.string.pluginType_url)+socialPluginType;
        Log.e("++++SSOCIAL REGISTRAION URLLLLLLLL",socialRegistration_url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, socialRegistration_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("onResponse",""+response);
                        getResponseData(response,socialPluginType);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        reportError(error);
                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void getResponseData(JSONObject response,String socialPluginType ) {
        try {
            JSONObject returnResponse = response.getJSONObject("users");
            userId = returnResponse.getString("id");
            Toast.makeText(this,returnResponse.getString("message"),Toast.LENGTH_SHORT).show();
            afterGettingUserDetails(socialPluginType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void afterGettingUserDetails(String socialPluginType) {
        if(socialPluginType.contentEquals("google")){
            if (skipVisible) {
                Intent begin = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(begin);
                session.createLoginSession(userId,personPhoto, personName, personEmail, "", socialPluginType);
                finish();
            } else {
                Intent googleIntent = getIntent();
                setResult(Activity.RESULT_OK, googleIntent);
                session.createLoginSession(userId,personPhoto, personName, personEmail, "", socialPluginType);
                finish();
            }
        }else if(socialPluginType.contentEquals("facebook")){
            if (skipVisible) {
                Intent begin = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(begin);
                session.createLoginSession(userId,mFbid, mFullname, mEmail, mGender,socialPluginType);
                finish();
            } else {
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);
                session.createLoginSession(userId,mFbid, mFullname, mEmail, mGender, socialPluginType);
                finish();
            }
        }else{
            if (skipVisible) {
                Intent begin = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(begin);
                session.createLoginSession(smonksId,"Display Picture",fullname,email,"",socialPluginType);
                finish();
            }else{
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);
                session.createLoginSession(smonksId,"Display Picture",fullname,email,"",socialPluginType);
                finish();
            }

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
                                JSONObject json_object, GraphResponse response) {
                            if (response != null) {
                                Bundle bFacebookData = getFacebookData(json_object);
                                 mFbid = bFacebookData.getString("idFacebook");
                                String firstName = bFacebookData.getString("first_name");
                                String lastName = bFacebookData.getString("last_name");
                                 mFullname = bFacebookData.getString("first_name") + " " + bFacebookData.getString("last_name");
                                 mEmail = bFacebookData.getString("email");
                                 mGender = bFacebookData.getString("gender");

                                sendUserDetails(firstName,lastName,mEmail,"facebook");
                            } else {

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
            bundle.putString("idFacebook", object.getString("id"));
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

        } catch (JSONException e) {
            Log.e("exp", "" + e.getLocalizedMessage());
        }
        return bundle;
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
         email = mEmailView.getText().toString();
         password = mPasswordView.getText().toString();

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
           sendUserLoginDetails();

        }
    }

    private void sendUserLoginDetails() {
        String userLoginUrl =getResources().getString(R.string.main_url)+getResources().getString(R.string.user_login_url)+email+getResources().getString(R.string.password_url)+password;
        Log.e("++++USER LOGIN URL++++++++",userLoginUrl);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, userLoginUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("onResponse",""+response);
                        getLoginResponseData(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        reportError(error);
                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void getLoginResponseData(JSONObject response) {
        try {
            JSONObject returnResponse = response.getJSONObject("users");

            identity = returnResponse.getBoolean("logged_in");
            Toast.makeText(this,returnResponse.getString("message"),Toast.LENGTH_SHORT).show();
            if(identity){
                smonksId = returnResponse.getString("id");
                first_name = returnResponse.getString("first_name");
                last_name = returnResponse.getString("last_name");
                fullname = first_name+" "+last_name;
                afterGettingUserDetails("default_login");
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
            Log.e("onBackPressed", "___________Entered__________");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gplus_signin_button:
                signInWithGoogle();
                break;

            case R.id.skip_button:
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(main);
                finish();
                break;

            case R.id.sign_in_button:
            attemptLogin();
                break;

            case R.id.sign_up:
                Intent register = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(register);
                break;
            case R.id.forgot_password:
                Intent forgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPassword);
                break;
        }
    }
    private void reportError(VolleyError error) {
        Log.e("response Errorhome", error + "");
        if (error instanceof NoConnectionError) {
            Log.d("NoConnectionError>>>>>>>>>", "NoConnectionError.......");
        } else if (error instanceof AuthFailureError) {
            Log.d("AuthFailureError>>>>>>>>>", "AuthFailureError.......");
        } else if (error instanceof ServerError) {
            Log.d("ServerError>>>>>>>>>", "ServerError.......");
        } else if (error instanceof NetworkError) {
            Log.d("NetworkError>>>>>>>>>", "NetworkError.......");
        } else if (error instanceof ParseError) {
            Log.d("ParseError>>>>>>>>>", "ParseError.......");
        }else if (error instanceof TimeoutError) {
            Log.d("TimeoutError>>>>>>>>>", "TimeoutError.......");
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Unable to connect with the server.Try again after some time.")
                    .setTitle("Server Error");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }
}