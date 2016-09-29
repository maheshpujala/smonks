package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Adapters.RecyclerAdapter;
import com.example.maheshpujala.sillymonks.R;

/**
 * Created by maheshpujala on 13/9/16.
 */
public class CategoryFragment extends Fragment {
    public CategoryFragment() {
        // Required empty public constructor
    }

    RecyclerView home_list;
    private final String[] values = new String[]{
            "TollyWood",
            "BollyWood",
            "KollyWood",
            "MollyWood",
            "HollyWood",
            "Creators",
    };
    private final Integer[] images = new Integer[]{

            R.drawable.tollywood,
            R.drawable.bollywood,
            R.drawable.kollywood,
            R.drawable.mollywood,
            R.drawable.hollywood,
            R.drawable.creators
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        home_list = (RecyclerView) view.findViewById(R.id.category_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        home_list.setLayoutManager(layoutManager);

        home_list.setAdapter(new RecyclerAdapter(getActivity(), values, images,1));

        home_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

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
    }
}