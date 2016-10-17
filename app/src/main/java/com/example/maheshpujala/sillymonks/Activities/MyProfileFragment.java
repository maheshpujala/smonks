package com.example.maheshpujala.sillymonks.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.Adapters.ImageAdapter;
import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.CircleImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.maheshpujala.sillymonks.Model.SessionManager.KEY_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.mopub.common.SharedPreferencesHelper.getSharedPreferences;

/**
 * Created by maheshpujala on 28/9/16.
 */
public class MyProfileFragment extends Fragment {
    TextView f_name,email,gender,name,logout;
    CircleImageView pic;
    List<UserData> user_data;
    SessionManager session;
    private CallbackManager mFacebookCallbackManager;
    private GoogleApiClient mGoogleApiClient;


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
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        f_name = (TextView) view.findViewById(R.id.user_name);
        name = (TextView) view.findViewById(R.id.user_fname);
        email = (TextView) view.findViewById(R.id.user_email);
        gender = (TextView) view.findViewById(R.id.user_gender);
        logout = (TextView) view.findViewById(R.id.logout_text);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_data.get(0).getLoginType().contains("facebook")) {
                    LoginManager.getInstance().logOut();
                }else{
                    if (mGoogleApiClient.isConnected())
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }
                session.logoutUser();
                getActivity().finish();
            }
        });

        pic = (CircleImageView) view.findViewById(R.id.user_image);

        f_name.setText(user_data.get(0).getName());
        name.setText(user_data.get(0).getName());
        email.setText(user_data.get(0).getEmail());
        gender.setText(user_data.get(0).getGender());
        if(user_data.get(0).getId().contains("jpg")){
            Picasso.with(getContext()).load(user_data.get(0).getId()).into(pic);
        }else{
            Picasso.with(getContext()).load("https://graph.facebook.com/" +user_data.get(0).getId()+ "/picture?type=large").into(pic);
        }
    }

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(getActivity().getApplication());

    }


}