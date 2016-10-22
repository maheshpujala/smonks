package com.example.maheshpujala.sillymonks.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.TouchImageView;

import java.util.ArrayList;

/**
 * Created by maheshpujala on 29/9/16.
 */
public class FullScreenImageAdapter extends PagerAdapter {
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<String> image;
    public FullScreenImageAdapter(Activity activity, ArrayList<String> image) {
        this._activity = activity;
        mContext = _activity;
        this.image = image;
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        TouchImageView imgDisplay;

        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fragment_fullscreen_image, container, false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.fullscreen_imageView);
//        for (int i=0;i<image.size();i++){
         Glide.with(imgDisplay.getContext()).load(image.get(position)).into(imgDisplay);
//        }
        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }
    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
    // is our job to remove the page from the container, which is normally the
    // ViewPager itself.  Since all our pages are persistent, we do nothing to the
    // contents of our "views" ArrayList.
    @Override
    public void destroyItem (ViewGroup container, int position, Object object)
    {
        View view = (View) object;
        ((ViewPager) container).removeView(view);
    }
}

