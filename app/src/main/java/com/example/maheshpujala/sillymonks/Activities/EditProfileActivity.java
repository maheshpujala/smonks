package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by maheshpujala on 19/10/16.
 */

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button save_changes,change_pwd,change_picture,changePwdOnScreen,change_pwd_on_screen;
    EditText firstName_edit,lastName_edit,emailAddress_edit,gender_edit,newPwdEdit,currentPwdEdit,renewPwdEdit;
    String fname,lname,email,gender;
    SessionManager session;
    List<UserData> user_data;
    private Animation mInAnimation, mOutAnimation;
    public ScrollView mScrollView;
    private View mChangePwdLayout;
    private int CURRENT_SCREEN = 0;
    private int UPDATE_SCREEN = 0;
    private int CHANGE_PWD_SCREEN = 1;
    TextView toolbar_title;
    Boolean password_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(this);
        user_data = session.getUserDetails();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Edit Profile");

        Intent i=getIntent();
        fname= i.getExtras().getString("first");
        lname = i.getExtras().getString("last");
        email= i.getExtras().getString("email");
        gender= i.getExtras().getString("gender");

        mInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.screen_in);
        mOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.screen_out);
        mChangePwdLayout = findViewById(R.id.layout_change_pwd);
        mScrollView = (ScrollView) findViewById(R.id.main_scroll);
        changePwdOnScreen = (Button) findViewById(R.id.change_pwd_on_screen);

        CURRENT_SCREEN = UPDATE_SCREEN;

        currentPwdEdit = (EditText) findViewById(R.id.current_pwd);
        newPwdEdit = (EditText) findViewById(R.id.new_pwd);
        renewPwdEdit = (EditText) findViewById(R.id.re_new_pwd);
        firstName_edit =(EditText)findViewById(R.id.firstName_edit);
        lastName_edit = (EditText) findViewById(R.id.lastName_edit);
        emailAddress_edit =(EditText)findViewById(R.id.emailAddress_edit);
        emailAddress_edit.setKeyListener(null);
        gender_edit = (EditText)findViewById(R.id.gender_edit);

        save_changes=(Button)findViewById(R.id.save_changes);
        save_changes.setOnClickListener(this);
        change_pwd = (Button)findViewById(R.id.change_pwd);
        change_pwd.setOnClickListener(this);
        change_picture = (Button)findViewById(R.id.change_picture);
        change_picture.setOnClickListener(this);
        change_pwd_on_screen = (Button)findViewById(R.id.change_pwd_on_screen);
        change_pwd_on_screen.setOnClickListener(this);
        firstName_edit.setText(fname);
        lastName_edit.setText(lname);
        emailAddress_edit.setText(email);
        gender_edit.setText(gender);

        setAnimationForViews();

    }


    @Override
    public void onBackPressed() {
        Log.e("onBackPressed","___________Entered__________");
        if (CURRENT_SCREEN == UPDATE_SCREEN) {
            finish();
        } else if (CURRENT_SCREEN == CHANGE_PWD_SCREEN) {
            mChangePwdLayout.setAnimation(mOutAnimation);
            mChangePwdLayout.startAnimation(mOutAnimation);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (CURRENT_SCREEN == UPDATE_SCREEN) {
                finish();
            } else if (CURRENT_SCREEN == CHANGE_PWD_SCREEN) {
                mChangePwdLayout.setAnimation(mOutAnimation);
                mChangePwdLayout.startAnimation(mOutAnimation);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.save_changes:
                sendRequestForUpdate();
                break;
            case R.id.change_picture:
                break;
            case R.id.change_pwd:
                CURRENT_SCREEN = CHANGE_PWD_SCREEN;
               toolbar_title.setText("Change Password");
                mChangePwdLayout.setVisibility(View.VISIBLE);
                mChangePwdLayout.setAnimation(mInAnimation);
                mChangePwdLayout.startAnimation(mInAnimation);

                break;
            case R.id.change_pwd_on_screen:
                changePwd();
                break;
        }
    }

    private void sendRequestForUpdate() {
        final String fullname = firstName_edit.getText().toString()+" "+lastName_edit.getText().toString();
        String updateUserDetails_url =getResources().getString(R.string.main_url)+getResources().getString(R.string.update_user_url)+user_data.get(0).getSmonksId()+"&"+getResources().getString(R.string.firstname_url)
                +firstName_edit.getText().toString()+getResources().getString(R.string.lastname_url)+lastName_edit.getText().toString();
        Log.e("++++updateUserDetails_url URLLLLLLLL",updateUserDetails_url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, updateUserDetails_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("onResponse",""+response);
                        try {
                            Toast.makeText(getApplication(),response.getString("message"),Toast.LENGTH_SHORT).show();
                            session.createLoginSession(user_data.get(0).getSmonksId(),"Display Picture",fullname,user_data.get(0).getEmail(),"","default_login");
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
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (CURRENT_SCREEN == UPDATE_SCREEN) {
                    finish();
                } else if (CURRENT_SCREEN == CHANGE_PWD_SCREEN) {
                    mChangePwdLayout.setAnimation(mOutAnimation);
                    mChangePwdLayout.startAnimation(mOutAnimation);
                }
                break;
            default:
                break;
        }
        return false;
    }
    public void changePwd() {
Log.e("changed password","Entered");
        String currentpassword_String = currentPwdEdit.getText().toString();
        String newPassword_String = newPwdEdit.getText().toString();
        String reNewpassword_String = renewPwdEdit.getText().toString();

        if (currentpassword_String.equals("")) {
            Toast.makeText(getApplication(),"Enter Current password",Toast.LENGTH_SHORT).show();
        } else if (newPassword_String.equals("")) {
            Toast.makeText(getApplication(),"Enter New password",Toast.LENGTH_SHORT).show();
        } else if (!newPassword_String.equals(reNewpassword_String)) {
            Toast.makeText(getApplication(),"New Password and Confirm Password does not match",Toast.LENGTH_SHORT).show();
        }else if(newPassword_String.length()<=5 || newPassword_String.length()>20){
            Toast.makeText(this,"Your New password length should be 6 to 20 characters",Toast.LENGTH_SHORT).show();
        }
        else {
            String updatePasswordUrl =getResources().getString(R.string.main_url)+
                    getResources().getString(R.string.change_password_url)+currentpassword_String+
                    getResources().getString(R.string.user_id_url)+user_data.get(0).getSmonksId()+
                    getResources().getString(R.string.new_password_url)+newPassword_String+
                    getResources().getString(R.string.confirm_password_url)+reNewpassword_String;
            Log.e("++++uupdatePasswordUrl URLLLLLLLL",updatePasswordUrl);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, updatePasswordUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("onResponse",""+response);
                            try {
                                password_verify =response.getBoolean("is_changed");
                                if(password_verify){
                                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                                    if (CURRENT_SCREEN == CHANGE_PWD_SCREEN) {
                                        mChangePwdLayout.setAnimation(mOutAnimation);
                                        mChangePwdLayout.startAnimation(mOutAnimation);
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                                }
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
// Add the request to the RequestQueue.
            VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
        }

    }


    public void setAnimationForViews() {

        mInAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
//					mForgetLayout.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);

            }
        });

        mOutAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//					mSignInLayout.setVisibility(View.VISIBLE);
                mChangePwdLayout.setVisibility(View.GONE);
                toolbar_title.setText("Edit Profile");
                CURRENT_SCREEN = UPDATE_SCREEN;
            }
        });
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