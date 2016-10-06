package com.example.maheshpujala.sillymonks.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.maheshpujala.sillymonks.Adapters.RecycleAdapter;
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
public class CategorFragment extends Fragment {
    public CategorFragment() {
        // Required empty public constructor
    }

    RecyclerView myRecyclerView;
    List<Article> articles;
    RecyclerView.LayoutManager layoutManager;
    private RecycleAdapter mAdapter;
    protected Handler handler;
    String category_name,category_id;
    LinkedHashMap categories,articles_total_count;
    List<Article> moreArticles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        articles = (List<Article>) this.getArguments().getSerializable("articles");
        categories = (LinkedHashMap) this.getArguments().getSerializable("categories");
        articles_total_count= (LinkedHashMap) this.getArguments().getSerializable("articles_total_count");
        category_name = this.getArguments().getString("category_name");
        category_id = (String) categories.get(category_name);
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myRecyclerView = (RecyclerView) view.findViewById(R.id.category_list);
        myRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        myRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecycleAdapter(getActivity(), articles,1,myRecyclerView);
        myRecyclerView.setAdapter(mAdapter);


        // Add the scroll listener
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                articles.add(null);
                mAdapter.notifyItemInserted(articles.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        articles.remove(articles.size() - 1);
                        mAdapter.notifyItemRemoved(articles.size());

                        //add items one by one
                        int start = articles.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            articles.add(new Article("Student " + i, "AndroidStudent" + i + "@gmail.com","name "+i));
                            mAdapter.notifyItemInserted(articles.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });
    }

    private List<Article> getExtraData() {
        moreArticles = new ArrayList<Article>();
        String lastid =articles.get(articles.size()-1).getId() ;
        String url_extra_data =getResources().getString(R.string.main_url)+getResources().getString(R.string.articles_load_extra)+category_id+getResources().getString(R.string.lastId)+lastid+getResources().getString(R.string.os_tag);
        //   Log.e("--------------------","cat_name"+category_name+"\n Cat_id"+category_id+"\n Article count"+articles.size());
        Log.e("Extra  articles URL $$$$",""+url_extra_data);

// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url_extra_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray extra_articles_list = response.getJSONArray("articles");
                            Log.e("Extra  articles Length-----------",""+extra_articles_list.length());

                            for (int k = 0; k < extra_articles_list.length(); k++) {
                                JSONObject articles_json = extra_articles_list.getJSONObject(k);

                                moreArticles.add(new Article(articles_json.getString("id"),
                                        articles_json.getString("title"),
                                        articles_json.getString("large")));
                                Log.e("!!!!!! INCREASING More articles Length!!!!!!!!!!!!!!!!!",""+moreArticles.size());

                            }
                        }catch (Exception e ){
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
        Log.e("More articles Length!!!!!!!!!!!!!!!!!",""+moreArticles.size());

        return moreArticles;
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



//
//=======================
//
//        package com.example.maheshpujala.sillymonks.Activities;
//
//        import android.app.Activity;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.support.v4.app.Fragment;
//        import android.support.v7.app.AlertDialog;
//        import android.support.v7.view.ContextThemeWrapper;
//        import android.support.v7.widget.LinearLayoutManager;
//        import android.support.v7.widget.RecyclerView;
//        import android.util.Log;
//        import android.view.GestureDetector;
//        import android.view.LayoutInflater;
//        import android.view.MotionEvent;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Toast;
//
//        import com.android.volley.AuthFailureError;
//        import com.android.volley.NetworkError;
//        import com.android.volley.NoConnectionError;
//        import com.android.volley.ParseError;
//        import com.android.volley.Request;
//        import com.android.volley.Response;
//        import com.android.volley.ServerError;
//        import com.android.volley.TimeoutError;
//        import com.android.volley.VolleyError;
//        import com.android.volley.toolbox.JsonObjectRequest;
//        import com.example.maheshpujala.sillymonks.Adapters.OnLoadMoreListener;
//        import com.example.maheshpujala.sillymonks.Adapters.RecyclerAdapter;
//        import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
//        import com.example.maheshpujala.sillymonks.Model.Article;
//        import com.example.maheshpujala.sillymonks.R;
//        import com.example.maheshpujala.sillymonks.Utils.EndlessRecyclerViewScrollListener;
//
//        import org.json.JSONArray;
//        import org.json.JSONObject;
//
//        import java.util.ArrayList;
//        import java.util.LinkedHashMap;
//        import java.util.List;
//
///**
// * Created by maheshpujala on 13/9/16.
// */
//public class CategoryFragment extends Fragment {
//    public CategoryFragment() {
//        // Required empty public constructor
//    }
//
//    RecyclerView myRecyclerView;
//    List<Article> articles;
//    RecyclerView.LayoutManager layoutManager;
//    private RecyclerAdapter mAdapter;
//    protected Handler handler;
//    String category_name,category_id;
//    LinkedHashMap categories,articles_total_count;
//    List<Article> moreArticles;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        articles = (List<Article>) this.getArguments().getSerializable("articles");
//        categories = (LinkedHashMap) this.getArguments().getSerializable("categories");
//        articles_total_count= (LinkedHashMap) this.getArguments().getSerializable("articles_total_count");
//        category_name = this.getArguments().getString("category_name");
//        category_id = (String) categories.get(category_name);
//        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        myRecyclerView = (RecyclerView) view.findViewById(R.id.category_list);
//        myRecyclerView.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(getContext());
//        myRecyclerView.setLayoutManager(layoutManager);
//
//        mAdapter = new RecyclerAdapter(getActivity(), articles,1,myRecyclerView);
//        myRecyclerView.setAdapter(mAdapter);
//
//
//        myRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//            });
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                if (child != null && gestureDetector.onTouchEvent(e)) {
//                    int position = rv.getChildAdapterPosition(child);
//                    Toast.makeText(getContext(), "Clicked" + position, Toast.LENGTH_SHORT).show();
//                    Intent cat2art = new Intent(getActivity(), ArticleActivity.class);
//                    startActivity(cat2art);
//                }
//
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//
//        });
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
//                    final Runnable r = new Runnable() {
//                        public void run() {
//                            mAdapter.notifyItemRangeInserted(currentSize, articles.size() - 1);
//                        }
//                    };
//
//                    handler.post(r);
//                }
//
//
//            }
//        });
//    }
//
//    private List<Article> getExtraData() {
//        moreArticles = new ArrayList<Article>();
//        String lastid =articles.get(articles.size()-1).getId() ;
//        String url_extra_data =getResources().getString(R.string.main_url)+getResources().getString(R.string.articles_load_extra)+category_id+getResources().getString(R.string.lastId)+lastid+getResources().getString(R.string.os_tag);
//        //   Log.e("--------------------","cat_name"+category_name+"\n Cat_id"+category_id+"\n Article count"+articles.size());
//        Log.e("Extra  articles URL $$$$",""+url_extra_data);
//
//// Request a JsonObject response from the provided URL.
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, url_extra_data, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray extra_articles_list = response.getJSONArray("articles");
//                            Log.e("Extra  articles Length-----------",""+extra_articles_list.length());
//
//                            for (int k = 0; k < extra_articles_list.length(); k++) {
//                                JSONObject articles_json = extra_articles_list.getJSONObject(k);
//
//                                moreArticles.add(new Article(articles_json.getString("id"),
//                                        articles_json.getString("title"),
//                                        articles_json.getString("large")));
//                                Log.e("!!!!!! INCREASING More articles Length!!!!!!!!!!!!!!!!!",""+moreArticles.size());
//
//                            }
//                        }catch (Exception e ){
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        reportError(error);
//                    }
//                });
//// Add the request to the RequestQueue.
//        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
//        Log.e("More articles Length!!!!!!!!!!!!!!!!!",""+moreArticles.size());
//
//        return moreArticles;
//    }
//    private void reportError(VolleyError error) {
//        Log.e("response Errorhome", error + "");
//        if (error instanceof NoConnectionError) {
//            Log.d("NoConnectionError>>>>>>>>>", "NoConnectionError.......");
//        } else if (error instanceof AuthFailureError) {
//            Log.d("AuthFailureError>>>>>>>>>", "AuthFailureError.......");
//        } else if (error instanceof ServerError) {
//            Log.d("ServerError>>>>>>>>>", "ServerError.......");
//        } else if (error instanceof NetworkError) {
//            Log.d("NetworkError>>>>>>>>>", "NetworkError.......");
//        } else if (error instanceof ParseError) {
//            Log.d("ParseError>>>>>>>>>", "ParseError.......");
//        }else if (error instanceof TimeoutError) {
//            Log.d("TimeoutError>>>>>>>>>", "TimeoutError.......");
//            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog));
//
//            // 2. Chain together various setter methods to set the dialog characteristics
//            builder.setMessage("Unable to connect with the server.Try again after some time.")
//                    .setTitle("Server Error");
//
//            // 3. Get the AlertDialog from create()
//            AlertDialog dialog = builder.create();
//
//            dialog.show();
//        }
//    }
//}