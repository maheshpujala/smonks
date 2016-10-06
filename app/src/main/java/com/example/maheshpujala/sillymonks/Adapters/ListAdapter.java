package com.example.maheshpujala.sillymonks.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by maheshpujala on 29/8/16.
 */
public class ListAdapter extends ArrayAdapter {
    private  List<String> title ,image,id;
    private  Activity context;

    public ListAdapter(Activity context, List<String> title, List<String> image,List<String> id) {
        super(context,R.layout.listview_home);
        this.context = context;
        this.title = title;
        this.image=image;
        this.id=id;
         }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView;
            if (position == 0) {

                rowView = inflater.inflate(R.layout.listview_dummy, null);
                LinearLayout blank =(LinearLayout) rowView.findViewById(R.id.blank);
            } else {
                rowView = inflater.inflate(R.layout.listview_home, null);
                TextView wood_name_tv = (TextView) rowView.findViewById(R.id.wood_name_sillymonks);
                ImageView cover_image = (ImageView) rowView.findViewById(R.id.wood_cover_image);

                wood_name_tv.setText(title.get(position));

                Picasso.with(this.context).load(image.get(position)).fit().into(cover_image);
                Log.e("picasso","+"+image.get(position));

            }

        return rowView;
    }
}