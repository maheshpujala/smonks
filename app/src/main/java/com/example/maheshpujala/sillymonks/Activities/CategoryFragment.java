package com.example.maheshpujala.sillymonks.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.maheshpujala.sillymonks.Adapters.OnLoadMoreListener;
import com.example.maheshpujala.sillymonks.Adapters.RecyclerAdapter;
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private RecyclerAdapter mAdapter;
    protected Handler handler;
    String category_name,category_id;
    LinkedHashMap categories,articles_total_count;
    List<Article> moreArticles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articles = (List<Article>) this.getArguments().getSerializable("articles");
        categories = (LinkedHashMap) this.getArguments().getSerializable("categories");
        articles_total_count= (LinkedHashMap) this.getArguments().getSerializable("articles_total_count");
        category_name = this.getArguments().getString("category_name");
        category_id = (String) categories.get(category_name);

        Log.e("ONCREATE  CATEGORY Fragment","\n");
        Log.e("\ncategory name== "+category_name,"\narticles_SIZE== "+articles.size());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myRecyclerView = (RecyclerView) view.findViewById(R.id.category_list);
        myRecyclerView.setHasFixedSize(true);

        if (category_name.equalsIgnoreCase("celebrities") || category_name.equalsIgnoreCase("gallery")){
            RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            myRecyclerView.setLayoutManager(gridLayoutManager);
        }else{
            layoutManager = new LinearLayoutManager(getContext());
            myRecyclerView.setLayoutManager(layoutManager);
        }



        mAdapter = new RecyclerAdapter(getActivity(), articles,1,myRecyclerView,category_name, (String) articles_total_count.get(category_name));
        myRecyclerView.setAdapter(mAdapter);


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
                    int position = rv.getChildAdapterPosition(child);
                    Toast.makeText(getContext(), "Clicked" + position, Toast.LENGTH_SHORT).show();
                    Intent cat2art = new Intent(getActivity(), ArticleActivity.class);
                    startActivity(cat2art);
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
                Log.e("onLoadMore!!!!!!!!!!!","Entered");
                Log.e("Current Category -----",((CategoryActivity)getActivity()).currentTabTitle);
                Log.e("Category Name ",category_name);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (((CategoryActivity)getActivity()).currentTabTitle.trim().equalsIgnoreCase(category_name) && Integer.parseInt((String) articles_total_count.get(category_name)) > articles.size() ){
                            Log.e("----------Total articles size before getting extra data-----------"," ="+articles.size());
                            Log.e(">>>>>>>>>>>>>>>","getting extra data from url<<<<<<<<<<<<<<");

                        // Call you API, then update the result into dataModels, then call adapter.notifyDataSetChanged().
                        //Update the new data into list object

                             getExtraData();

                        }

                    }
                }, 1000);

            }
        });




//        // Add the scroll listener
//        myRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                Log.e("onLoadMore","page nO:- "+page+"\ntotalItemsCount:-"+totalItemsCount);
//                Log.e("((CategoryActivity)getActivity()).getCurrentTabTitle()-----",((CategoryActivity)getActivity()).getCurrentTabTitle());
//                Log.e("Category Name ",category_name);
//                if (((CategoryActivity)getActivity()).getCurrentTabTitle().trim().equalsIgnoreCase(category_name) && Integer.parseInt((String) articles_total_count.get(category_name)) > totalItemsCount ){
//                    Log.e("getting Extra Data","getting extra data");
//
//                    List<Article> newArticles =  getExtraData();
//                    Log.e("Total articles Length-----------",""+articles.size());
//                    Log.e("New   articles Length##########",""+newArticles.size());
//
//
//                    final int currentSize = mAdapter.getItemCount();
//                    articles.addAll(newArticles);
//                    Log.e("Total  articles New Length$$$$$$$$$",""+articles.size());
//
//                    Handler handler = new Handler();
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.notifyItemRangeInserted(currentSize, articles.size() - 1);
//                        }
//                    }, 2000);
//
//                }
//
//
//            }
//        });
    }

    private void getExtraData() {
        moreArticles = new ArrayList<Article>();
        String lastId =articles.get(articles.size()-1).getId() ;
        final int previous_articles_count = articles.size();
        Log.e("@@@@@@@@@@@   categoryName @@@@@@@@@@@  "+category_name,"\n  Last ID  ="+lastId);

        //  Log.e("@@@@@@@@@@@  category_name"+category_name,"\n\n\nArticles Count"+articles.size());
        String url_extra_data =getResources().getString(R.string.main_url)+getResources().getString(R.string.articles_load_extra)+category_id+getResources().getString(R.string.lastId)+lastId+getResources().getString(R.string.os_tag);
        //   Log.e("--------------------","cat_name"+category_name+"\n Cat_id"+category_id+"\n Article title"+articles.get(0).getTitle());
        Log.e("Extra  articles URL $$$$",""+url_extra_data);

// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url_extra_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray extra_articles_list = response.getJSONArray("articles");
                            Log.e("===========Extra  articles JSONArray Length===========",""+extra_articles_list.length());

                            for (int k = 0; k < extra_articles_list.length(); k++) {
                                JSONObject articles_json = extra_articles_list.getJSONObject(k);

                                moreArticles.add(new Article(articles_json.getString("id"),
                                        articles_json.getString("title"),
                                        articles_json.getString("large")));
                                Log.e("!!!!!!!!! More articles IDS !!!!!!!!!!!!!!"," ="+moreArticles.get(k).getId());

                            }
                        }catch (Exception e ){
                            e.printStackTrace();
                        }


                        articles.addAll(moreArticles);
                        Log.e("$$$$$$$$$Total articles after getting from json$$$$$$$$$ "," ="+articles.size());
                       // mAdapter.notifyDataSetChanged();



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