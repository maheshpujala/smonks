package com.example.maheshpujala.sillymonks.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.example.maheshpujala.sillymonks.R;

/**
 * Created by maheshpujala on 23/8/16.
 */
public class ContactAndAdvertise extends AppCompatActivity implements View.OnClickListener {
    private EditText desc,pname,pmobile;
    private TextView header;
    private Button submit;
    private String person_name,mobile_no,description;


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