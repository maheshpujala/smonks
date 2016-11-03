package com.example.maheshpujala.sillymonks.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.maheshpujala.sillymonks.Adapters.ListAdapter;
import com.example.maheshpujala.sillymonks.Network.Connectivity;
import com.example.maheshpujala.sillymonks.Network.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.BounceListView;
import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by maheshpujala on 8/10/16.
 */


public class RelatedArticles extends AppCompatActivity implements SearchView.OnQueryTextListener {
    String categoryID,articleID,categoryName;
    List<String> fCategoryId,fCategoryName,fWoodId;
    String more_article_id,more_article_title,searchQuery,wood_id,identifyActivity,imageSize;
    BounceListView list_moreArticles;
    List<Article> articlesList,searchedArticlesList;
    TextView toolbar_title;
    SearchView searchView;
    ProgressDialog progressdialog;
    private MoPubAdAdapter mAdAdapter;
    ListAdapter lAdapter;
    MoPubStaticNativeAdRenderer adRenderer;
    MoPubNativeAdPositioning.MoPubServerPositioning adPositioning;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_articles);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressdialog = new ProgressDialog(RelatedArticles.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);
        progressdialog.show();

        // Set up a ViewBinder and MoPubNativeAdRenderer as above.
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.mopub_ad_unit)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        // Set up the positioning behavior your ads should have.
        adPositioning = MoPubNativeAdPositioning.serverPositioning();

        adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        String connectType=Connectivity.connectionType(RelatedArticles.this);
        if(Connectivity.isConnectedWifi(RelatedArticles.this)){
            imageSize = "original";
        }else if(connectType.equalsIgnoreCase("CDMA")||connectType.equalsIgnoreCase("EDGE")||connectType.equalsIgnoreCase("GPRS")||connectType.equalsIgnoreCase("1xRTT")){
            imageSize = "small";
        }
        else if (connectType.equalsIgnoreCase("HSDPA")||connectType.equalsIgnoreCase("HSPA")||connectType.equalsIgnoreCase("HSUPA")||connectType.equalsIgnoreCase("UMTS")||connectType.equalsIgnoreCase("HSPA+")){
            imageSize = "medium";
        }
        else if (connectType.equalsIgnoreCase("LTE")){
            imageSize = "large";
        }else{
            imageSize = "small";
        }

        toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        Intent getIds = getIntent();
        if(getIds !=null) {
             identifyActivity = getIds.getExtras().getString("identifyActivity");
            if (identifyActivity.equals("SearchResult")) {
                toolbar_title.setText("    "+getResources().getString(R.string.search_result));
                searchQuery  = getIds.getExtras().getString("searchQuery");
                if(searchQuery.length()>1){
                    sendSearchRequest(searchQuery);
                }
            }
            if (identifyActivity.equals("RelatedArticles")) {
                articleID = getIds.getExtras().getString("articleID");
                categoryID = getIds.getExtras().getString("categoryID");
                categoryName = getIds.getExtras().getString("categoryName");
                wood_id = getIds.getExtras().getString("wood_id");
                toolbar_title.setText(getResources().getString(R.string.related_articles));
                sendRequest(articleID,categoryID,wood_id);
            }
        }
        list_moreArticles = (BounceListView) findViewById(R.id.list_moreArticles);
        list_moreArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                progressdialog.show();
                int  originalPosition=  mAdAdapter.getOriginalPosition(position);

                Intent it = new Intent(RelatedArticles.this, ArticleActivity.class);
                it.putExtra("identifyActivity","RelatedArticles" );
                it.putExtra("selected_position",""+originalPosition);

                if (toolbar_title.getText().toString().trim().contains(getResources().getString(R.string.search_result))){
                    it.putExtra("articleID",searchedArticlesList.get(originalPosition).getId());
                    it.putExtra("categoryID",fCategoryId.get(originalPosition));
//                    it.putExtra("categoryName",fCategoryName.get(position));
                    it.putExtra("wood_id",fWoodId.get(originalPosition));
                    it.putExtra("articles", (Serializable) searchedArticlesList);
                }else if(toolbar_title.getText().toString().trim().contains(getResources().getString(R.string.related_articles))){
                    it.putExtra("articleID",articlesList.get(originalPosition).getId());
                    it.putExtra("categoryID",categoryID);
//                    it.putExtra("categoryName",categoryName);
                    it.putExtra("wood_id",wood_id);
                    it.putExtra("articles", (Serializable) articlesList);
                }
                Log.e("articleID="+articleID+"categoryID="+categoryID,"categoryName="+categoryName+"wood_id="+wood_id);
                startActivity(it);
            }
        });
    }



    private void sendSearchRequest(String query) {
        String url_cat_list = null;
        try {
            url_cat_list = getResources().getString(R.string.main_url)+getResources().getString(R.string.search_url)+ URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url_cat_list, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getSearchData(response);
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

    private void getSearchData(JSONObject response) {
        try {
            JSONArray searchedArticles = response.getJSONArray("articles");
            fCategoryId = new ArrayList<>();
            fCategoryName = new ArrayList<>();
            fWoodId = new ArrayList<>();
            searchedArticlesList = new ArrayList<>();

            for (int i = 0; i < searchedArticles.length(); i++) {
                JSONObject search = searchedArticles.getJSONObject(i);
                Article a = new Article(search.getString("id"),
                        search.getString("title"),
                        search.getString(imageSize),
                        search.getString("published_at"),
                        search.getString("likes_count"),
                        search.getString("comments_count"));
                a.setFirstCategoryName(search.getString("first_cageory_name"));
                searchedArticlesList.add(a);
                fCategoryId.add(search.getString("first_cageory_id"));
                fWoodId.add(search.getString("first_wood_id"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapter(searchedArticlesList);
        progressdialog.dismiss();
    }
    MoPubNative.MoPubNativeNetworkListener moPubNativeListener = new MoPubNative.MoPubNativeNetworkListener() {
        @Override
        public void onNativeLoad(NativeAd nativeAd) {
            // ...
            Log.e("onNativeLoad",""+nativeAd);
        }

        @Override
        public void onNativeFail(NativeErrorCode errorCode) {
            // ...
             Log.e("onNativeFail",""+errorCode);

        }
    };

    private void setAdapter(List<Article> itemsForAdapter) {
        lAdapter = new ListAdapter(this, itemsForAdapter);
        // Set up the MoPubAdAdapter
        mAdAdapter = new MoPubAdAdapter(this, lAdapter, adPositioning);
        mAdAdapter.registerAdRenderer(adRenderer);
        mAdAdapter.loadAds("4af8f892da924673aa0d9db92b49cc10");
        list_moreArticles.setAdapter(mAdAdapter);
    }

    private void sendRequest(String articleID,String categoryID,String wood_id) {
        String moreArticlesUrl = getResources().getString(R.string.main_url)+getResources().getString(R.string.more_articles_url)+articleID+getResources().getString(R.string.categoryId_url)+categoryID+getResources().getString(R.string.wood_id_url)+wood_id;
// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, moreArticlesUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getData(response);

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

    public void getData(JSONObject json) {
        try {
            JSONArray more_articles_array = json.getJSONArray("articles");

            articlesList = new ArrayList<>();
            for (int k = 0; k < more_articles_array.length(); k++) {
                JSONObject relatedArticles = more_articles_array.getJSONObject(k);
                Article a = new Article(relatedArticles.getString("id"),
                        relatedArticles.getString("title"),
                        relatedArticles.getString(imageSize),
                        relatedArticles.getString("published_at"),
                        relatedArticles.getString("likes_count"),
                        relatedArticles.getString("comments_count"));
                a.setFirstCategoryName(relatedArticles.getString("first_cageory_name"));
                articlesList.add(a);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapter(articlesList);
        progressdialog.dismiss();

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
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getApplication(), R.style.myDialog));

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Unable to connect with the server.Try again after some time.")
                    .setTitle("Server Error");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
            progressdialog.dismiss();
        }
    }
    @Override
    public void onResume(){
        progressdialog.dismiss();
       // mAdAdapter.loadAds("e6784f6a4d7a4b84a9134580a6dbc400");
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        if(mAdAdapter != null){
            mAdAdapter.destroy();
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        searchView=
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        if(identifyActivity.contains("SearchResult")){
            searchView.setIconified(false);
            searchView.setQuery(searchQuery,true);
            searchView.setOnQueryTextListener(this);
            progressdialog.dismiss();
        }else{
            MenuItem search = menu.findItem(R.id.action_search);
            search.setVisible(false);
        }
        return true;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.onActionViewCollapsed();
        sendSearchRequest(query);
        Toast.makeText(getApplication(), "TEXT SUBMITTED"+query, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(query.length()>3) {
            sendSearchRequest(query);
        }
        return false;
    }
}
