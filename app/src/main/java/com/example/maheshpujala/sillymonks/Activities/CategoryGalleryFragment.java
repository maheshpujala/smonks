package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Adapters.RecyclerAdapter;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by maheshpujala on 28/9/16.
 */
public class CategoryGalleryFragment extends Fragment {

    public CategoryGalleryFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myRecyclerView = (RecyclerView) view.findViewById(R.id.category_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        myRecyclerView.setLayoutManager(layoutManager);

      //  home_list.setAdapter(new RecyclerAdapter(getActivity(), Names, Images,2));

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
                    Intent cat2art = new Intent(getActivity(), GalleryActivity.class);
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
    }
}
