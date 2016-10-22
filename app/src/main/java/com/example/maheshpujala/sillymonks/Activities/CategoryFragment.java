package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.maheshpujala.sillymonks.Adapters.OnLoadMoreListener;
import com.example.maheshpujala.sillymonks.Adapters.RecyclerAdapter;
import com.example.maheshpujala.sillymonks.Network.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by maheshpujala on 13/9/16.
 */
public class CategoryFragment extends Fragment {
    public CategoryFragment() {
        // Required empty public constructor
    }

    RecyclerView myRecyclerView;
    List<Article> articles;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter mAdapter;
    String category_name,category_id,wood_id;
    HashMap articles_total_count;
    List<Article> moreArticles;
    private MoPubRecyclerAdapter moPubAdapter;
    private MoPubAdAdapter mAdAdapter;
    HashMap categories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articles = (List<Article>) this.getArguments().getSerializable("articles");
        categories = (HashMap) this.getArguments().getSerializable("categories");
        articles_total_count= (HashMap) this.getArguments().getSerializable("articles_total_count");
        category_name = this.getArguments().getString("category_name");
        category_id = (String) categories.get(category_name);
        wood_id = this.getArguments().getString("wood_id");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.category_list);
        myRecyclerView.setHasFixedSize(true);
//        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                myRecyclerView.stopNestedScroll();
//
//            }
//        });
//        myRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//
//            int dragthreshold = 30;
//            int downX;
//            int downY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        downX = (int) event.getRawX();
//                        downY = (int) event.getRawY();
//                        Log.e("VIEW Y____DOWN",""+v);
//                        Log.e("_DOWN_downX=="+downX,"_DOWN_downY=="+downY);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int distanceX = Math.abs((int) event.getRawX() - downX);
//                        int distanceY = Math.abs((int) event.getRawY() - downY);
//                        Log.e("VIEW Y____MOVE",""+v.getY());
//
//                        Log.e("distanceX="+distanceX,"distanceY="+distanceY);
////
////                        if (distanceY > distanceX && distanceY > dragthreshold) {
////                            viewPager.getParent().requestDisallowInterceptTouchEvent(false);
////                            scrollView.getParent().requestDisallowInterceptTouchEvent(true);
////                        } else if (distanceX > distanceY && distanceX > dragthreshold) {
////                            viewPager.getParent().requestDisallowInterceptTouchEvent(true);
////                            scrollView.getParent().requestDisallowInterceptTouchEvent(false);
////                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        downX = (int) event.getRawX();
//                        downY = (int) event.getRawY();
//                        Log.e("VIEW Y____UP",""+v.getY());
//
//                        Log.e("UP downX=="+downX,"UP downY=="+downY);
//
////                        scrollView.getParent().requestDisallowInterceptTouchEvent(false);
////                        viewPager.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });
        mAdapter = new RecyclerAdapter(getActivity(), articles,myRecyclerView,category_name, (String) articles_total_count.get(category_name));
        moPubAdapter = new MoPubRecyclerAdapter(getActivity(), mAdapter,
                MoPubNativeAdPositioning.serverPositioning());
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.mopub_ad_unit)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build()
        );
        moPubAdapter.loadAds("e6784f6a4d7a4b84a9134580a6dbc400");
        moPubAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);
        myRecyclerView.setAdapter(moPubAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

            layoutManager = new LinearLayoutManager(getContext());
            myRecyclerView.setLayoutManager(layoutManager);



        myRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int positio = rv.getChildAdapterPosition(child);
                    Log.e("CHILD POSITON",""+positio);
                    Log.e("moPubAdapter.isAd(positio)=",""+moPubAdapter.isAd(positio));
                    if(!moPubAdapter.isAd(positio)){
                        int  position=  moPubAdapter.getOriginalPosition(positio);
                        Log.e("ORGINAL POSITON"+position,"articles List"+articles);

                        Article selectedArticle = articles.get(position);
                        String article_id = selectedArticle.getId();
                        Intent cat2art = new Intent(getActivity(), ArticleActivity.class);
                        cat2art.putExtra("identifyActivity","categoryArticles" );
                        cat2art.putExtra("articleID",article_id);
                        cat2art.putExtra("categoryID",category_id);
                        cat2art.putExtra("categoryName",category_name);
                        cat2art.putExtra("wood_id",wood_id);
                        cat2art.putExtra("articles", (Serializable) articles);
                        cat2art.putExtra("selected_position",""+position);

                        startActivity(cat2art);
                    }

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

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("RECYCLER ADAPTER++++++",""+category_name );

              //  if (((CategoryActivity)getActivity()).currentTabTitle.trim().equalsIgnoreCase(category_name) && Integer.parseInt((String) articles_total_count.get(category_name)) > articles.size() ){
                if (Integer.parseInt((String) articles_total_count.get(category_name)) > articles.size() ){
                // Call you API, then update the result into dataModels, then call adapter.notifyDataSetChanged().
                    //Update the new data into list object

                    getExtraData();
                }
            }
        });
        moPubAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        // MoPub recommends loading knew ads when the user returns to your activity.
        moPubAdapter.loadAds("e6784f6a4d7a4b84a9134580a6dbc400");
        super.onResume();
        }
    private void getExtraData() {
        moreArticles = new ArrayList<Article>();
        String lastId =articles.get(articles.size()-1).getId() ;
        final int previous_articles_count = articles.size();

        String url_extra_data =getResources().getString(R.string.main_url)+getResources().getString(R.string.articles_load_extra)+category_id+getResources().getString(R.string.lastId)+lastId+getResources().getString(R.string.os_tag);

// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url_extra_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray extra_articles_list = response.getJSONArray("articles");

                            for (int k = 0; k < extra_articles_list.length(); k++) {
                                JSONObject articles_json = extra_articles_list.getJSONObject(k);

                                moreArticles.add(new Article(articles_json.getString("id"),
                                        articles_json.getString("title"),
                                        articles_json.getString("large"),
                                        articles_json.getString("published_at"),
                                        articles_json.getString("likes_count"),
                                        articles_json.getString("comments_count")));

                            }
                        }catch (Exception e ){
                            e.printStackTrace();
                        }

                        articles.addAll(moreArticles);

                        mAdapter.notifyItemRangeInserted(previous_articles_count, moreArticles.size());
                        mAdapter.setLoaded();

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