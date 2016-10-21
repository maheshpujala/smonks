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

import com.bumptech.glide.Glide;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by maheshpujala on 29/8/16.
 */
public class ListAdapter extends BaseAdapter {
    private  List<String> wood_titles ,wood_images,wood_ids;
    private  Activity context;
    private  int response;
    String responseCode;
    List<Article> articlesList;
    TextView wood_name_tv,time_ago,comments_count,likes_count;
    ImageView cover_image;
    String comment_text,like_text,Days,Hours,Time,Minutes;
    Map commentsMap;


    public ListAdapter(Activity context, List<String> wood_titles, List<String> wood_images,List<String> wood_ids,int response) {
        this.context = context;
        this.wood_titles = wood_titles;
        this.wood_images=wood_images;
        this.wood_ids=wood_ids;
        this.response=response;

    }
    public ListAdapter(Activity context, List<Article> articlesList) {
        this.context = context;
        this.articlesList = articlesList;
    }
    public ListAdapter(Activity context, List<String> wood_titles,int response) {
        this.context = context;
        this.wood_titles = wood_titles;
        this.response = response;
    }
    public ListAdapter(Activity context, Map commentsMap, int response) {
        this.context = context;
        this.commentsMap = commentsMap;
        this.response = response;
    }

    @Override
    public int getCount() {
        int count;
        if (response == 1){
            count = wood_titles.size();
        }else if (response == 2){
            count = wood_titles.size();

        }else if (response == 3){
            count = commentsMap.size();
        }else{
            count = articlesList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (response == 1){

            rowView = inflater.inflate(R.layout.listview_navigation, null);
            TextView nav_text = (TextView) rowView.findViewById(R.id.nav_text);
            nav_text.setText(wood_titles.get(position));

        }else if(response == 2){

            if (position == 0) {

                rowView = inflater.inflate(R.layout.listview_dummy, null);
                LinearLayout blank =(LinearLayout) rowView.findViewById(R.id.blank);
            } else {
                rowView = inflater.inflate(R.layout.listview_home, null);
                TextView wood_name_tv = (TextView) rowView.findViewById(R.id.wood_name_sillymonks);
                ImageView cover_image = (ImageView) rowView.findViewById(R.id.wood_cover_image);

                wood_name_tv.setText(wood_titles.get(position));

                Glide.with(this.context).load(wood_images.get(position)).into(cover_image);

            }
        }else if(response ==3){
            rowView = inflater.inflate(R.layout.listview_comments, null);

            ImageView user_dp = (ImageView) rowView.findViewById(R.id.user_dp);
            TextView userName = (TextView) rowView.findViewById(R.id.userName);
            TextView rating_text = (TextView) rowView.findViewById(R.id.rating_text);
            TextView textViewComment = (TextView) rowView.findViewById(R.id.textViewComment);

            List<String> comments;
            comments= (List<String>) commentsMap.get(position);

            Glide.with(this.context).load(comments.get(0)).into(user_dp);
            userName.setText(comments.get(1));
            rating_text.setText(comments.get(2));
            textViewComment.setText(comments.get(3));
        }else{
            rowView = inflater.inflate(R.layout.recyclerview_category, null);
             wood_name_tv = (TextView) rowView.findViewById(R.id.wood_name_sillymonks);
             cover_image = (ImageView) rowView.findViewById(R.id.wood_cover_image);
            time_ago = (TextView) rowView.findViewById(R.id.time_ago);
            comments_count =(TextView)rowView.findViewById(R.id.comments_count);
            likes_count  =(TextView)rowView.findViewById(R.id.likes_count);

            Article a = articlesList.get(position);

            if(Integer.parseInt(a.getcommentsCount()) == 0){
                comment_text = "No Comments";
            }else if(Integer.parseInt(a.getcommentsCount()) == 1){
                comment_text = a.getcommentsCount()+" Comment";
            }else{
                comment_text = a.getcommentsCount()+" Comments";
            }

            if(Integer.parseInt(a.getlikesCount()) == 0){
                like_text = "No Likes";
            }else if(Integer.parseInt(a.getlikesCount()) == 1){
                like_text = a.getlikesCount()+" Like";
            }else{
                like_text = a.getlikesCount()+" Likes";
            }

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            String currentTime =dates.format(new Date());

            try {

                //Dates to compare
                String CurrentDate = currentTime;
                String PublishedDate = a.getpublishedAt();

                Date date1;
                Date date2;


                //Setting dates
                date1 = dates.parse(CurrentDate);
                date2 = dates.parse(PublishedDate);

                //Comparing dates
                long difference = Math.abs(date1.getTime() - date2.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);

                //Convert long to String
                Days = Long.toString(differenceDates);
                if (Days.contentEquals("0")) {
                    long differenceHours = difference / (60 * 60 * 1000);
                    Hours = Long.toString(differenceHours);
                    if (Hours.contentEquals("0")) {
                        long differenceMinutes = difference / (60 * 1000);
                        Minutes = Long.toString(differenceMinutes);
                        if (Minutes.contentEquals("1")) {
                            Time = Minutes + " minute ago";
                        }else{
                            Time = Minutes + " minutes ago";
                        }
                    }else if (Hours.contentEquals("1")) {
                        Time = Hours + " hour ago";
                    }else{
                        Time = Hours + " hours ago";
                    }
                } else if(Days.contentEquals("1")){
                    Time = Days + " day ago";
                }else{
                    Time = Days + " days ago";
                }

            } catch (Exception exception) {
                Log.e("DIDN'T WORK", "exception " + exception);
            }

            time_ago.setText(Time);
            comments_count.setText(comment_text);
            likes_count.setText(like_text);
            wood_name_tv.setText(articlesList.get(position).getTitle());
            Glide.with(this.context).load(articlesList.get(position).getBannerMedia()).into(cover_image);
        }

        return rowView;
    }
}