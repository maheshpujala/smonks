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
import java.util.List;

/**
 * Created by maheshpujala on 29/8/16.
 */
public class ListAdapter extends BaseAdapter {
    private final List<String> title ,image,id;
    private final Activity context;
    private String[] strarray,id_array;

    public ListAdapter(Activity context, List<String> title, List<String> image,List<String> id) {
        this.context = context;
        this.title = title;
        this.image=image;
        this.id=id;
        for(int i=0;i<=image.size();i++) {
            strarray = new String[image.size()];
            strarray = image.toArray(strarray);
        }
        for(int i=0;i<=id.size();i++) {
            id_array = new String[id.size()];
            id_array = id.toArray(id_array);
        }
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
    public long getItemId(int position) {
        return Long.parseLong(id_array[position]);
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
                TextView wood_name = (TextView) rowView.findViewById(R.id.wood_name_sillymonks);
                ImageView cover_image = (ImageView) rowView.findViewById(R.id.wood_cover_image);
                wood_name.setText(title.get(position));

             Picasso.with(this.context).load(strarray[position]).fit().into(cover_image);

            }

        return rowView;
    }
}