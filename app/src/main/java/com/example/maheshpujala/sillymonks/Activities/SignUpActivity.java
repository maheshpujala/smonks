package com.example.maheshpujala.sillymonks.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.maheshpujala.sillymonks.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maheshpujala on 18/10/16.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView home,toolbar_title;
    EditText Fname_holder,Lname_holder,Email_holder,Pswd_holder,mobileNo_holder;
    Button signUp_button,signIn_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Sign Up");

        signIn_button = (Button) findViewById(R.id.signIn_button);
        signUp_button = (Button) findViewById(R.id.signUp_button);

        signIn_button.setOnClickListener(this);
        signUp_button.setOnClickListener(this);

        Fname_holder = (EditText) findViewById(R.id.fname_holder);
        Lname_holder = (EditText) findViewById(R.id.lname_holder);
        mobileNo_holder = (EditText) findViewById(R.id.mobileNo_holder);
        Email_holder = (EditText) findViewById(R.id.email_holder);
        Pswd_holder = (EditText) findViewById(R.id.password_holder);
        Pswd_holder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signUp || id == EditorInfo.IME_NULL) {
                    signUp_button.performClick();
                    return true;
                }
                return false;
            }
        });



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signIn_button:
                this.finish();
                break;
            case R.id.signUp_button:
                if(checkValidation()){
                    sendUserRegistration();
                }
                break;
        }
    }

    private void sendUserRegistration() {
        String registration_url =getResources().getString(R.string.main_url)+getResources().getString(R.string.user_registration_url)+getResources().getString(R.string.firstname_url)+Fname_holder.getText().toString()+getResources().getString(R.string.lastname_url)+Lname_holder.getText().toString()+getResources().getString(R.string.email_url)+Email_holder.getText().toString()+getResources().getString(R.string.password_url)+Pswd_holder.getText().toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, registration_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("onResponse",""+response);
                        getResponseData(response);
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

    private void getResponseData(JSONObject response) {
        try {
            JSONObject returnResponse = response.getJSONObject("users");
            Toast.makeText(this,returnResponse.getString("message"),Toast.LENGTH_SHORT).show();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkValidation() {
        if(Fname_holder.getText().toString().length()<=2){
            Toast.makeText(this,"Your First name should be at least 3 characters.",Toast.LENGTH_SHORT).show();
            return false;
        }else if(Lname_holder.getText().toString().length()<=2){
            Toast.makeText(this,"Your Last name should be at least 3 characters.",Toast.LENGTH_SHORT).show();
            return false;
        }else if(mobileNo_holder.getText().toString().length() != 10){
            Toast.makeText(this,"Your Mobile Number is invalid",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!isEmailValid(Email_holder.getText().toString())){
            Toast.makeText(this,"Your Email format is invalid",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Pswd_holder.getText().toString().length()<=5 || Pswd_holder.getText().toString().length()>20){
            Toast.makeText(this,"Your password length should be 6 to 20 characters",Toast.LENGTH_SHORT).show();
            return false;
        }else if (Fname_holder.getText().toString().contains(" ") || Lname_holder.getText().toString().contains(" ")){
            Toast.makeText(this,"No Spaces Allowed",Toast.LENGTH_SHORT).show();
            return false;
        }

            return true;
        }

    private boolean isEmailValid(String s) {
        return s != null && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed","___________Entered__________");
        this.finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getApplication(), R.style.myDialog));

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Unable to connect with the server.Try again after some time.")
                    .setTitle("Server Error");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }
}
