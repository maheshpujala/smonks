package com.example.maheshpujala.sillymonks.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.maheshpujala.sillymonks.Network.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.HelperMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by maheshpujala on 19/10/16.
 */

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button save_changes,change_pwd,change_picture,change_pwd_on_screen;
    EditText firstName_edit,lastName_edit,emailAddress_edit,gender_edit,newPwdEdit,currentPwdEdit,renewPwdEdit;
    String fname,lname,email,gender,smonksID;
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
    int RESULT_LOAD_IMAGE = 555;
    ImageView profilePic;
    Bitmap decodedProfilePicture;
    private static final int REQUEST_WRITE_STORAGE = 112;
    ProgressDialog progress;
    String picturePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(this);
        user_data = session.getUserDetails();
        smonksID=user_data.get(0).getSmonksId();

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

        CURRENT_SCREEN = UPDATE_SCREEN;

        profilePic = (ImageView) findViewById(R.id.profilePic);
        if(user_data.get(0).getId().length()>30) {
            decodedProfilePicture = HelperMethods.decodeBase64(user_data.get(0).getId());
            profilePic.setImageBitmap(decodedProfilePicture);
        }
        change_pwd_on_screen = (Button)findViewById(R.id.change_pwd_on_screen);
        change_pwd_on_screen.setOnClickListener(this);
        save_changes=(Button)findViewById(R.id.save_changes);
        save_changes.setOnClickListener(this);
        currentPwdEdit = (EditText) findViewById(R.id.current_pwd);
        newPwdEdit = (EditText) findViewById(R.id.new_pwd);
        renewPwdEdit = (EditText) findViewById(R.id.re_new_pwd);
        firstName_edit =(EditText)findViewById(R.id.firstName_edit);
        lastName_edit = (EditText) findViewById(R.id.lastName_edit);
        emailAddress_edit =(EditText)findViewById(R.id.emailAddress_edit);
        emailAddress_edit.setKeyListener(null);
        gender_edit = (EditText)findViewById(R.id.gender_edit);
        gender_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.update || id == EditorInfo.IME_NULL) {
                    save_changes.performClick();
                    return true;
                }
                return false;
            }
        });
        renewPwdEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.update || id == EditorInfo.IME_NULL) {
                    change_pwd_on_screen.performClick();
                    return true;
                }
                return false;
            }
        });

        change_pwd = (Button)findViewById(R.id.change_pwd);
        change_pwd.setOnClickListener(this);
        change_picture = (Button)findViewById(R.id.change_picture);
        change_picture.setOnClickListener(this);
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
                int permissionCheck = ContextCompat.checkSelfPermission(EditProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    makeRequest();
                }else{
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }

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
        profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        progress = new ProgressDialog(EditProfileActivity.this);
        progress.setTitle("Uploading");
        progress.setMessage("Please wait...");
        progress.show();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(picturePath) ;
                String content_type  = getMimeType(f.getPath());

                String file_path = f.getAbsolutePath();
                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id","28")
                        .addFormDataPart("type",content_type)
                        .addFormDataPart("profile_picture",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                        .build();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://192.168.1.15:3000/api/v1/PostUserProfileUpdate")
                        .post(request_body)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    if(!response.isSuccessful()){
                        throw new IOException("Error : "+response);
                    }

                    progress.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

//    private void sendRequestForUpdate() {
//        Bitmap bitmap = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
//
//        final  String profileImage = HelperMethods.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
//        String updateUserDetailsURL ="http://192.168.1.15:3000/api/v1/PostUserProfileUpdate";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,updateUserDetailsURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("response++++++++++++",response);
//                        Toast.makeText(EditProfileActivity.this,response,Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(EditProfileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("id",smonksID);
//                params.put("first_name",firstName_edit.getText().toString());
//                params.put("last_name",lastName_edit.getText().toString());
//                params.put("profile_image",profileImage);
//                return params;
//            }
//
//        };
//
//        VolleyRequest.getInstance().addToRequestQueue(stringRequest);
//    }


//    private void sendRequestForUpdate() {
//        final String fullname = firstName_edit.getText().toString()+" "+lastName_edit.getText().toString();
//        String updateUserDetails_url =getResources().getString(R.string.main_url)+getResources().getString(R.string.update_user_url)+smonksID+"&"+getResources().getString(R.string.firstname_url)
//                +firstName_edit.getText().toString()+getResources().getString(R.string.lastname_url)+lastName_edit.getText().toString();
//        Log.e("++++updateUserDetails_url URLLLLLLLL",updateUserDetails_url);
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.POST, updateUserDetails_url, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("onResponse",""+response);
//                        try {
//                            Toast.makeText(getApplication(),response.getString("message"),Toast.LENGTH_SHORT).show();
//
//                            Bitmap bitmap = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
//                            String profileImage = HelperMethods.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
//                            session.createLoginSession(user_data.get(0).getSmonksId(),profileImage,fullname,user_data.get(0).getEmail(),"","default_login");
//                            finish();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        reportError(error);
//                    }
//                });
//// Add the request to the RequestQueue.
//        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
//    }


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image
            profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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