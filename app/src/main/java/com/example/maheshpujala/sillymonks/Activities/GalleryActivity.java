package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.example.maheshpujala.sillymonks.Adapters.ImageAdapter;
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.R;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements MoPubView.BannerAdListener {
  String celebrityID,wood_id,categoryName,id,image_url;
    LinkedHashMap imagesMap;
    GridView gridview;
    private MoPubView moPubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView  toolbar_title = (TextView)findViewById(R.id.toolbar_title);


        Intent getIds = getIntent();
        celebrityID = getIds.getExtras().getString("celebrityID");
        wood_id = getIds.getExtras().getString("wood_id");
        categoryName = getIds.getExtras().getString("categoryName");

        toolbar_title.setText(categoryName);

        sendRequest(celebrityID,wood_id);


        gridview = (GridView) findViewById(R.id.gallery_view);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                   List<String> imagesList = new ArrayList<String>(imagesMap.values());

                    Toast.makeText(getApplicationContext(), "clicked ID " +imagesList.get(position) , Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(GalleryActivity.this, FullScreenActivity.class);
                    it.putStringArrayListExtra("imageURL", (ArrayList<String>) imagesList);
                    it.putExtra("clicked_image",position);
                startActivity(it);
            }
        });

        moPubView = (MoPubView) findViewById(R.id.mopub_adview);
        moPubView.setAdUnitId("6d4ed34709184518870d820a5dd5f27e");
        moPubView.loadAd();
        moPubView.setBannerAdListener(this);
    }

    private void sendRequest(String celebrityId, String woodId) {
        String galleryUrl =getResources().getString(R.string.main_url)+getResources().getString(R.string.gallery_url)+celebrityId+getResources().getString(R.string.wood_id_url)+woodId+getResources().getString(R.string.os_tag);

// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, galleryUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getData(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(GalleryActivity.this, R.style.myDialog));

                            // 2. Chain together various setter methods to set the dialog characteristics
                            builder.setMessage("Unable to connect with the server.Try again after some time.")
                                    .setTitle("Server Error");

                            // 3. Get the AlertDialog from create()
                            AlertDialog dialog = builder.create();

                            dialog.show();
                        }


                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void getData(JSONObject response) {
        try {
            JSONArray pictures = response.getJSONArray("pictures");
            imagesMap = new LinkedHashMap();

            for (int i = 0; i < pictures.length(); i++) {
                JSONObject jsonobject = pictures.getJSONObject(i);
                id = jsonobject.getString("id");
                image_url = jsonobject.getString("original");
                imagesMap.put(id,image_url);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridview.setAdapter(new ImageAdapter(getApplicationContext(),imagesMap));

    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed","___________Entered__________");
        this.finish();
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
    protected void onDestroy() {
        moPubView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {

    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {

    }

    @Override
    public void onBannerClicked(MoPubView banner) {

    }

    @Override
    public void onBannerExpanded(MoPubView banner) {

    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {

    }
}
