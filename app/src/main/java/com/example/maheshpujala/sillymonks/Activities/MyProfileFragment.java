package com.example.maheshpujala.sillymonks.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.example.maheshpujala.sillymonks.Network.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.CircleImageView;
import com.example.maheshpujala.sillymonks.Utils.HelperMethods;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by maheshpujala on 28/9/16.
 */
public class MyProfileFragment extends Fragment {
    TextView l_name,f_name,email,gender,name,logout,editProfile,GenderView,full_name;
    CircleImageView pic;
    List<UserData> user_data;
    SessionManager session;
    private GoogleApiClient mGoogleApiClient;
    Bitmap decodedProfilePicture;


    public MyProfileFragment() {
    // Required empty public constructor
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getContext());
        user_data = session.getUserDetails();

        if(user_data.get(0).getLoginType().contains("facebook")) {
            facebookSDKInitialize();
        }else{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GenderView  = (TextView) view.findViewById(R.id.GenderView);
        full_name = (TextView) view.findViewById(R.id.user_name);
        f_name = (TextView) view.findViewById(R.id.user_fname);
        l_name = (TextView) view.findViewById(R.id.user_lname);
        email = (TextView) view.findViewById(R.id.user_email);
        gender = (TextView) view.findViewById(R.id.user_gender);
        editProfile=(TextView)view.findViewById(R.id.edit_profile);
        if(user_data.get(0).getLoginType().contains("facebook") ||user_data.get(0).getLoginType().contains("google")){
            editProfile.setVisibility(View.GONE);
        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit_profile = new Intent(getContext(),EditProfileActivity.class);
                String firstName = f_name.getText().toString();
                String lastName = l_name.getText().toString();

                edit_profile. putExtra("first",firstName);
                edit_profile. putExtra("last",lastName);
                edit_profile. putExtra("email",email.getText());
                edit_profile. putExtra("gender",gender.getText());
                startActivity(edit_profile);
            }
        });

        logout = (TextView) view.findViewById(R.id.logout_text);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure ?")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(user_data.get(0).getLoginType().contains("facebook")) {
                                    LoginManager.getInstance().logOut();
                                }else if(user_data.get(0).getLoginType().contains("google")){
                                    if (mGoogleApiClient.isConnected())
                                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                }else{
                                    sendLogout();
                                }
                                session.logoutUser();
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });

        pic = (CircleImageView) view.findViewById(R.id.user_image);

       setData();
    }

    private void sendLogout() {
        String logout_url =getResources().getString(R.string.main_url)+getResources().getString(R.string.user_logout_url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, logout_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject returnResponse = response.getJSONObject("message");
                            Toast.makeText(getContext(),returnResponse.getString("message"),Toast.LENGTH_LONG).show();

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


    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        CallbackManager mFacebookCallbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(getActivity().getApplication());

    }
    @Override
    public void onResume(){
        super.onResume();
        Log.e("ON RESUME ", "FREAGMENT");
        setData();
    }
    private void setData() {
        session = new SessionManager(getContext());
        user_data = session.getUserDetails();

        Log.e("SESSION ",""+user_data);
        full_name.setText(user_data.get(0).getFirstName()+" "+user_data.get(0).getLastName());
        f_name.setText(user_data.get(0).getFirstName());
        l_name.setText(user_data.get(0).getLastName());
        email.setText(user_data.get(0).getEmail());
        if(user_data.get(0).getGender().length()<1){
            GenderView.setVisibility(View.GONE);
        }else{
            GenderView.setVisibility(View.VISIBLE);
            gender.setText(user_data.get(0).getGender());
        }

        if(user_data.get(0).getLoginType().contains("google")){
            Glide.with(pic.getContext()).load(user_data.get(0).getId()).into(pic);
        }else if (user_data.get(0).getLoginType().contains("facebook")){
            Glide.with(pic.getContext()).load("https://graph.facebook.com/" +user_data.get(0).getId()+ "/picture?type=large").into(pic);
        }else if(user_data.get(0).getLoginType().contains("default_login")) {
            if(user_data.get(0).getId().contains("http")) {
                Glide.with(pic.getContext()).load(user_data.get(0).getId()).into(pic);
            }else {
                Log.e("DEFAULT LOGIN","ENTERED FOR DECODING");
                decodedProfilePicture = HelperMethods.decodeBase64(user_data.get(0).getId());
                pic.setImageBitmap(decodedProfilePicture);
            }
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
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog));

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Unable to connect with the server.Try again after some time.")
                    .setTitle("Server Error");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }
}