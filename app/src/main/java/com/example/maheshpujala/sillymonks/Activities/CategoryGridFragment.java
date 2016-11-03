package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.maheshpujala.sillymonks.Adapters.RecyclerAdapter;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maheshpujala on 28/9/16.
 */
public class CategoryGridFragment extends Fragment {

    public CategoryGridFragment() {
        // Required empty public constructor
    }

    RecyclerView myRecyclerView;
    List<Article> articles;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter mAdapter;
    String category_name,category_id,wood_id;
    HashMap categories,articles_total_count;
    List<Article> moreArticles;


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
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_category, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myRecyclerView = (RecyclerView) view.findViewById(R.id.category_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        myRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapter(getActivity(), articles,myRecyclerView,category_name, (String) articles_total_count.get(category_name),2);
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
                    Article selectedArticle = articles.get(position);
                    String article_id = selectedArticle.getId();
                    if (category_name.equalsIgnoreCase("gallery")) {
                        Intent cat2gal = new Intent(getActivity(), GalleryActivity.class);
                        cat2gal.putExtra("celebrityID", article_id);
                        cat2gal.putExtra("wood_id", wood_id);
                        cat2gal.putExtra("categoryName", category_name);
                        startActivity(cat2gal);
                    } else {
                        Intent cat2art = new Intent(getActivity(), ArticleActivity.class);
                        cat2art.putExtra("identifyActivity","categoryActivity");
                        cat2art.putExtra("articleID", article_id);
                        cat2art.putExtra("categoryID", category_id);
                        cat2art.putExtra("categoryName", category_name);
                        cat2art.putExtra("wood_id", wood_id);
                        cat2art.putExtra("articles", (Serializable) articles);
                        cat2art.putExtra("selected_position", "" + position);
                        Log.e("selected_position in Category Fragment", "" + position);

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

    }


}