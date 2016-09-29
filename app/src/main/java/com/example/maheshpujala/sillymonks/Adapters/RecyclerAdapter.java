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
    private final int responseCode;


    TextView wood_name,grid_text;
    ImageView cover_image,grid_image;

    public RecyclerAdapter(Activity context, String[] web, Integer[] image,int responseCode) {
        this.context = context;
        this.web = web;
        this.image=image;
        this.responseCode=responseCode;


        Log.e("Recycler Adapter", "Entered");
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.e("Recycler Adapter", "onCreateViewHolder");
        if (responseCode == 1) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_category, viewGroup, false);
            return new ViewHolder(view);
        }else {
            View gallery_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_gallery, viewGroup, false);
            return new ViewHolder(gallery_view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        Log.e("Recycler Adapter", "onBindViewHolder");
        holder.setIsRecyclable(false);
        if (responseCode == 1) {
            wood_name.setText(web[position]);
            cover_image.setImageResource(image[position]);
        }else{
            grid_text.setText(web[position]);
            grid_image.setImageResource(image[position]);
        }
    }

    @Override
    public int getItemCount() {
        return web.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            Log.e("Recycler Adapter", "ViewHolder");
            if (responseCode == 1) {
                wood_name = (TextView) view.findViewById(R.id.wood_name_sillymonks);
                cover_image = (ImageView) view.findViewById(R.id.wood_cover_image);
            } else {
                grid_text = (TextView) view.findViewById(R.id.grid_text);
                grid_image = (ImageView) view.findViewById(R.id.grid_image);
            }
        }
    }
}
