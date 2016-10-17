package com.example.maheshpujala.sillymonks.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.R;
import com.squareup.picasso.Picasso;

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
        // TODO Auto-generated method stub
        return ImageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<String> imagesList = new ArrayList<String>(ImageUrl.values());
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_gallery, null);
        } else {
            grid = (View) convertView;
        }
        ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
        Picasso.with(this.mContext).load(imagesList.get(position)).into(imageView);

        return grid;
    }
}