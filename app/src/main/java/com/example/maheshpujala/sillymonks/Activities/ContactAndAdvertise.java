package com.example.maheshpujala.sillymonks.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;

/**
 * Created by maheshpujala on 23/8/16.
 */
public class ContactAndAdvertise extends AppCompatActivity implements View.OnClickListener {
    EditText pname, pmobile, desc;
    TextView header;
    Button submit;
    String person_name,mobile_no,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_advertise_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        header = (TextView) findViewById(R.id.heading);
        pname = (EditText) findViewById(R.id.name);
        pmobile = (EditText) findViewById(R.id.mobile);
        desc = (EditText) findViewById(R.id.description);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
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
            Toast.makeText(this, "clicked SUBMIT BUTTON"+person_name +mobile_no, Toast.LENGTH_SHORT).show();
            checkData();
        }
    }

    private void checkData() {

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
    }

    private boolean isMobileValid(String mobile_no) {
        //TODO: Replace this with your own logic
        return mobile_no.length() >= 10;
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