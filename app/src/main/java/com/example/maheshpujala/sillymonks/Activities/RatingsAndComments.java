package com.example.maheshpujala.sillymonks.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by maheshpujala on 2/11/16.
 */

public class RatingsAndComments extends AppCompatActivity implements View.OnClickListener {
    RatingBar ratingBar;
    EditText commentsHolder;
    TextView ratingText;
    Button submit_button;
    String articleID,userID,averageRating;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Ratings & Comments");
        Intent intentData = getIntent();
        articleID = intentData.getExtras().getString("articleID");
        userID = intentData.getExtras().getString("user_ID");


        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingText = (TextView)findViewById(R.id.user_rating_text);
        commentsHolder= (EditText)findViewById(R.id.comment_holder);
        submit_button = (Button)findViewById(R.id.submit_button);
        submit_button.setOnClickListener(this);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingText.setText(""+rating);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                sendRatings_Comments();
            break;
        }

    }

    private void sendRatings_Comments() {
        String  sendRatingandCommentsUrl = null;
        try {
            sendRatingandCommentsUrl = getResources().getString(R.string.main_url)+getResources().getString(R.string.ratng_comnt_url)+articleID
                    + getResources().getString(R.string.userId_url)+userID+getResources().getString(R.string.rating_url)+ratingText.getText().toString()+getResources().getString(R.string.comment_url)+ URLEncoder.encode(commentsHolder.getText().toString(), "UTF-8");
            Log.e("sendRatingandCommentsUrl",sendRatingandCommentsUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Request a JsonObject response from the provided URL.

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sendRatingandCommentsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(RatingsAndComments.this,message,Toast.LENGTH_LONG).show();
                            averageRating = response.getString("average_rating");
                            closeActivity();
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
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }
    private void closeActivity() {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("average_rating",averageRating);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
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
        } else if (error instanceof TimeoutError) {
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
