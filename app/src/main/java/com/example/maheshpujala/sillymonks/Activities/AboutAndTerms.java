package com.example.maheshpujala.sillymonks.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.R;

/**
 * Created by maheshpujala on 23/8/16.
 */
public class AboutAndTerms extends AppCompatActivity {
    private TextView header;
    private TextView contenttodisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_terms_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        header = (TextView) findViewById(R.id.heading);
        contenttodisplay = (TextView) findViewById(R.id.content);
        setHeader();
    }
    private void setHeader() {
        int requestCode = getIntent().getExtras().getInt("requestCode");
        Log.e("requestCode",""+requestCode);
        if(requestCode == 1) {
            header.setText("About Us");
            contenttodisplay.setText(R.string.silly_about_us);

        }
        else if (requestCode == 2){
            header.setText("Terms And Conditions");
            contenttodisplay.setText(R.string.silly_terms_conditions);

        }
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