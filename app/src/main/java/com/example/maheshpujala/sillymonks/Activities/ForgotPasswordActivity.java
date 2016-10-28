package com.example.maheshpujala.sillymonks.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.DefaultRetryPolicy;
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

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email_address_holder;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        progressdialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Forgot Password");

       final Button send_button=(Button)findViewById(R.id.send_button);
        email_address_holder =(EditText)findViewById(R.id.email_address_holder);
        email_address_holder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    send_button.performClick();
                    return true;
                }
                return false;
            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmailValid(email_address_holder.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Your Email format is invalid",Toast.LENGTH_SHORT).show();
                }
                else{
                    sendRequest();
                    progressdialog.show();

                }
            }
        });
    }

    private void sendRequest() {
        String forgotPasswordUrl =getResources().getString(R.string.main_url)+getResources().getString(R.string.forgotPassword_url)+email_address_holder.getText().toString();
        Log.e("URL____",forgotPasswordUrl);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, forgotPasswordUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                            if(response.getBoolean("is_registered")){
                              finish();
                          }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        reportError(error);
                    }
                });
        int MY_SOCKET_TIMEOUT_MS = 15000;
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
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
            new AlertDialog.Builder(ForgotPasswordActivity.this)
                    .setTitle("Server Error")
                    .setMessage("Unable to connect with the server.Try again after some time.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

progressdialog.dismiss();
        }
    }
    private boolean isEmailValid(String s) {
        return s != null && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }
}
