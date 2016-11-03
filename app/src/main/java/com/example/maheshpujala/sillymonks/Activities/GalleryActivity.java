package com.example.maheshpujala.sillymonks.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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
import com.example.maheshpujala.sillymonks.Adapters.RecyclerAdapter;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.Network.Connectivity;
import com.example.maheshpujala.sillymonks.Network.VolleyRequest;
import com.example.maheshpujala.sillymonks.R;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GalleryActivity extends AppCompatActivity implements MoPubView.BannerAdListener {
  String celebrityID,wood_id,categoryName,id,image_url,imageSize;
    LinkedHashMap imagesMap;
    RecyclerView gridview;
    private MoPubView moPubView;
    ProgressDialog progressdialog;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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

        gridview = (RecyclerView) findViewById(R.id.gallery_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(GalleryActivity.this, 2);
        gridview.setLayoutManager(layoutManager);
        gridview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(GalleryActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    List<String> imagesList = new ArrayList<>(imagesMap.values());

                    Intent it = new Intent(GalleryActivity.this, FullScreenActivity.class);
                    it.putStringArrayListExtra("imageURL", (ArrayList<String>) imagesList);
                    it.putExtra("clicked_image",position);
                    startActivity(it);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });


        moPubView = (MoPubView) findViewById(R.id.mopub_adview);
        moPubView.setAdUnitId("b51640c1ff19442184e783f55e0428c3");
        moPubView.loadAd();
        moPubView.setBannerAdListener(this);
        progressdialog = new ProgressDialog(GalleryActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);
        progressdialog.show();
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
                            progressdialog.dismiss();
                        }


                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void getData(JSONObject response) {
        try {
            String connectType= Connectivity.connectionType(GalleryActivity.this);
            if(Connectivity.isConnectedWifi(GalleryActivity.this)){
                Log.e("isConnectedWifi CHECK",""+"ORIGINAL IMAGE!!!!!!!!!!!!!!");
                imageSize = "original";
            }else if(connectType.equalsIgnoreCase("CDMA")||connectType.equalsIgnoreCase("EDGE")||connectType.equalsIgnoreCase("GPRS")||connectType.equalsIgnoreCase("1xRTT")){
                Log.e("connectType CHECK",""+"SMALL IMAGE!!!!!!!!!!!!!!");
                imageSize = "small";

            }
            else if (connectType.equalsIgnoreCase("HSDPA")||connectType.equalsIgnoreCase("HSPA")||connectType.equalsIgnoreCase("HSUPA")||connectType.equalsIgnoreCase("UMTS")||connectType.equalsIgnoreCase("HSPA+")){
                Log.e("connectType CHECK",""+"Medium IMAGE!!!!!!!!!!!!!!");

                imageSize = "medium";

            }
            else if (connectType.equalsIgnoreCase("LTE")){
                Log.e("connectType CHECK",""+"Large IMAGE!!!!!!!!!!!!!!");

                imageSize = "large";

            }else{
                imageSize = "small";

            }
            JSONArray pictures = response.getJSONArray("pictures");
            imagesMap = new LinkedHashMap();

            for (int i = 0; i < pictures.length(); i++) {
                JSONObject jsonobject = pictures.getJSONObject(i);
                id = jsonobject.getString("id");
                image_url = jsonobject.getString(imageSize);
                imagesMap.put(id,image_url);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerAdapter mAdapter = new RecyclerAdapter(GalleryActivity.this,imagesMap,3);
        gridview.setAdapter(mAdapter);
        progressdialog.dismiss();

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
