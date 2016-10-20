package com.example.maheshpujala.sillymonks.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.maheshpujala.sillymonks.Adapters.Favourite_ListAdapter;
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.EnhancedListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by maheshpujala on 29/9/16.
 */

public class FavouritesFragment extends Fragment  {
    SessionManager session;
    List<UserData> userData;
    List<Article> favouritesList;
    LinkedHashMap favouritesMap;
    private Context mContext;
    private LinearLayout inflated_layout;
    private EnhancedListView favouritesListView;
    private TextView notAvailable;
    Favourite_ListAdapter favAdapter;
    public FavouritesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getContext());
        userData=session.getUserDetails();
        Log.e("USERR SILLYNMONS ID",""+userData.get(0).getSmonksId());

        sendRequestForFavourites();
    }

    private void sendRequestForFavourites() {
        String favourites_url =getResources().getString(R.string.main_url)+getResources().getString(R.string.user_favourites_url)+userData.get(0).getSmonksId();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, favourites_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       getFavourites(response);

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

    private void getFavourites(JSONObject response) {
        try {
            JSONArray favourites = response.getJSONArray("favourites");
            Log.e("JSON ARRAY favourites",""+favourites);
            favouritesList = new ArrayList<Article>();
            favouritesMap = new LinkedHashMap<String, Article>();
            for (int k = 0; k < favourites.length(); k++) {
                JSONObject favArticles = favourites.getJSONObject(k);
                favouritesMap.put(favArticles.getString("id"),new Article(favArticles.getString("id"),
                        favArticles.getString("first_cageory_id"),
                        favArticles.getString("first_cageory_name"),
                        favArticles.getString("first_wood_id"),
                        favArticles.getString("title"),
                        favArticles.getString("small"),
                        favArticles.getString("description")));
            }
            Log.e("fav==================",""+favouritesMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(favouritesMap.size()==0){
            notAvailable.setVisibility(View.VISIBLE);
        }else{
            favAdapter = new Favourite_ListAdapter(mContext, favouritesMap, FavouritesFragment.this, favouritesListView);
            favouritesListView.setAdapter(favAdapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        if(container ==null){
            return null;
        }
        // Inflate the layout for this fragment
        inflated_layout = (LinearLayout) inflater.inflate(R.layout.fragment_favourities, null);

        favouritesListView = (EnhancedListView) inflated_layout.findViewById(R.id.favourites_listview);
        notAvailable = (TextView) inflated_layout.findViewById(R.id.not_available);

        return inflated_layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        favouritesListView.setDismissCallback(new EnhancedListView.OnDismissCallback() {

            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
                final String item = (String) favAdapter.getItem(position);
                favAdapter.remove(position);
                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        favAdapter.insert(position, item);

                    }
                };
            }
        });

        favouritesListView.setUndoStyle(EnhancedListView.UndoStyle.SINGLE_POPUP);
        favouritesListView.setUndoHideDelay(8000);
        favouritesListView.enableSwipeToDismiss();
        favouritesListView.setSwipingLayout(R.id.favourite_relative_main);
        favouritesListView.setSwipeDirection(EnhancedListView.SwipeDirection.BOTH);
        favouritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getContext(),"NOT YET IMPLEMENTED",Toast.LENGTH_SHORT).show();
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
            new AlertDialog.Builder(getContext())
                    .setTitle("Server Error")
                    .setMessage("Unable to connect with the server.Try again after some time.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }
}