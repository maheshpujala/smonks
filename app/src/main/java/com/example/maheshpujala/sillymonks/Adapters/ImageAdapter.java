package com.example.maheshpujala.sillymonks.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.maheshpujala.sillymonks.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by maheshpujala on 28/9/16.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final LinkedHashMap ImageUrl;

    public ImageAdapter(Context c,LinkedHashMap ImageUrl ) {
        mContext = c;
        this.ImageUrl = ImageUrl;
    }

    @Override
    public int getCount() {
        return ImageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<String> imagesList = new ArrayList<>(ImageUrl.values());
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_gallery, null);
        } else {
            grid = convertView;
        }
        ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
        Glide.with(imageView.getContext()).load(imagesList.get(position)).dontAnimate().into(imageView);

        return grid;
    }
}