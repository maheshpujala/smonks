package com.example.maheshpujala.sillymonks.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.R;

/**
 * Created by maheshpujala on 27/9/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final String[] web;
    private final Activity context;
    private final Integer[] image;

    TextView wood_name;
    ImageView cover_image;

    public RecyclerAdapter(Activity context, String[] web, Integer[] image) {
        this.context = context;
        this.web = web;
        this.image=image;
        Log.e("Recycler Adapter", "Entered");
    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.e("Recycler Adapter", "onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_category, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        Log.e("Recycler Adapter", "onBindViewHolder");
        holder.setIsRecyclable(false);
        wood_name.setText(web[position]);
        cover_image.setImageResource(image[position]);
    }

    @Override
    public int getItemCount() {
        return web.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view) {
            super(view);
            Log.e("Recycler Adapter", "ViewHolder");

            wood_name = (TextView) view.findViewById(R.id.wood_name_sillymonks);
             cover_image = (ImageView) view.findViewById(R.id.wood_cover_image);
        }
    }
}
