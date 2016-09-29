package com.example.maheshpujala.sillymonks.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.Adapters.ImageAdapter;
import com.example.maheshpujala.sillymonks.R;
import com.squareup.picasso.Picasso;

/**
 * Created by maheshpujala on 28/9/16.
 */
public class MyProfileFragment extends Fragment {
    TextView f_name,email,gender,name;
    ImageView pic;
    Bundle myDataFromActivity;
    String name_b,email_b,id_b,gender_b;

    public MyProfileFragment() {
    // Required empty public constructor
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        MyProfileActivity activity = (MyProfileActivity) getActivity();
        myDataFromActivity = activity.getFBData();
        Log.e("bundle",""+myDataFromActivity);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        f_name = (TextView) view.findViewById(R.id.user_name);
        name = (TextView) view.findViewById(R.id.user_fname);
        email = (TextView) view.findViewById(R.id.user_email);
        gender = (TextView) view.findViewById(R.id.user_gender);

        pic = (ImageView) view.findViewById(R.id.user_image);

        name_b = myDataFromActivity.getString("FB_NAME");
        email_b = myDataFromActivity.getString("FB_EMAIL");
        id_b = myDataFromActivity.getString("FB_ID");
        gender_b = myDataFromActivity.getString("FB_GENDER");

        f_name.setText(name_b);
        name.setText(name_b);
        email.setText(email_b);
        gender.setText(gender_b);
        Picasso.with(getContext()).load("https://graph.facebook.com/" + id_b + "/picture?type=large").into(pic);
    }
}