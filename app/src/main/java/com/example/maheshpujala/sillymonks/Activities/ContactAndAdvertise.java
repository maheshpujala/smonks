package com.example.maheshpujala.sillymonks.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by maheshpujala on 23/8/16.
 */
public class ContactAndAdvertise extends AppCompatActivity implements View.OnClickListener {
    private EditText desc,pname,pmobile;
    private TextView header;
    private Button submit;
    private String person_name,mobile_no,description;
    ProgressDialog progressdialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_advertise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView  toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        toolbar_title.setText("Silly Monks");

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        header = (TextView) findViewById(R.id.heading);
        pname = (EditText) findViewById(R.id.name);
        pmobile = (EditText) findViewById(R.id.mobile);
        desc = (EditText) findViewById(R.id.description);
        desc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.submit || id == EditorInfo.IME_NULL) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });
        setHeader();
    }

    private void setHeader() {
        int requestCode = getIntent().getExtras().getInt("requestCode");
        Log.e("requestCode",""+requestCode);
        if(requestCode == 3) {
           header.setText("Contact Us");

       }
        else if (requestCode == 4){
           header.setText("Advertise With Us");
       }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.submit){
            // Store values at the time of the login attempt.
            person_name = pname.getText().toString();
            mobile_no  = pmobile.getText().toString();
            description = desc.getText().toString();

          if(!checkData()){
              sendRequest();
          }
        }
    }

    private void sendRequest() {
        progressdialog = new ProgressDialog(ContactAndAdvertise.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);
        progressdialog.show();
        final String requestType;
        if(header.getText().toString().equalsIgnoreCase("Contact Us")){
            requestType = "contact_us";
        }else{
            requestType = "advertise_with_us";
        }
        String ContactandAdvertise = null;
        try {
            ContactandAdvertise = getResources().getString(R.string.main_url)+getResources().getString(R.string.contact_url)+pname.getText().toString()+getResources().getString(R.string.mobile_tag)+pmobile.getText().toString()+getResources().getString(R.string.description_tag)+ URLEncoder.encode(desc.getText().toString(), "UTF-8")+getResources().getString(R.string.type_tag)+requestType+getResources().getString(R.string.os_tag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, ContactandAdvertise, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("onResponse",""+response);
                        try {
                            progressdialog.dismiss();
                            Toast.makeText(ContactAndAdvertise.this,response.getString("message"),Toast.LENGTH_LONG).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
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
    private boolean checkData() {
        boolean cancel = false;
        View focusView = null;

        // Check for a valid Name, if the user entered one.
        if (TextUtils.isEmpty(person_name)){
            pname.setError(getString(R.string.error_field_required));
            focusView = pname;
            cancel = true;
        }

        // Check for a valid mobile Number.
        else if (TextUtils.isEmpty(mobile_no)) {
            pmobile.setError(getString(R.string.error_field_required));
            focusView = pmobile;
            cancel = true;
        } else if (!isMobileValid(mobile_no)) {
            pmobile.setError(getString(R.string.error_invalid_number));
            focusView = pmobile;
            cancel = true;
        }
        else if(TextUtils.isEmpty(description)) {
            desc.setError(getString(R.string.error_desc_required));
            focusView = desc;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        Log.e("cancel",""+cancel);
        return cancel;
    }

    private boolean isMobileValid(String mobile_no) {

        return mobile_no.length() == 10;
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