package com.example.maheshpujala.sillymonks.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

/**
 * Created by maheshpujala on 29/8/16.
 */
public class ListAdapter extends BaseAdapter {
    private final String[] web;
    private final Activity context;
    private final Integer[] image;
    private final int responseCode;

    public ListAdapter(Activity context, String[] web,Integer[] image,int responseCode) {
        this.context = context;
        this.web = web;
        this.image=image;
        this.responseCode=responseCode;
    }

    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.e("getView","-------------------ENTERED----------------");

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView;
        if(responseCode == 1) {
            if (position == 0) {
                Log.e("getView", "POSITION CHECK++++0");

                rowView = inflater.inflate(R.layout.listview_dummy, null);

                LinearLayout blank = (LinearLayout) rowView.findViewById(R.id.blank);
            } else {
                Log.e("getView", "POSITION CHECK++++1");

                rowView = inflater.inflate(R.layout.listview_home, null);
                TextView wood_name = (TextView) rowView.findViewById(R.id.wood_name_sillymonks);
                ImageView cover_image = (ImageView) rowView.findViewById(R.id.wood_cover_image);
                wood_name.setText(web[position]);
                cover_image.setImageResource(image[position]);

            }
        }else{
                rowView = inflater.inflate(R.layout.listview_category, null);
                TextView wood_name = (TextView) rowView.findViewById(R.id.wood_name_sillymonks);
                ImageView cover_image = (ImageView) rowView.findViewById(R.id.wood_cover_image);
                wood_name.setText(web[position]);
                cover_image.setImageResource(image[position]);

            }


        return rowView;
    }
}