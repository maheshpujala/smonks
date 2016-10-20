package com.example.maheshpujala.sillymonks.Adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maheshpujala.sillymonks.Activities.CategoryActivity;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by maheshpujala on 27/9/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter {
    private  List<String> title;
    private final Activity context;
    private  List<String> image;
    private String[] imageArray;
    List<Article> articles;
    String category_name,total_articles_count,comment_text,like_text,Days,Hours,Time,Minutes,timeValue;


    TextView article_title,grid_text,time_ago,comments_count,likes_count;
    ImageView cover_image,grid_image;
    private final int AD_TYPE = 1;
    private final int CONTENT_TYPE = 0;


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public RecyclerAdapter(final Activity context, final List<Article> articles, RecyclerView recyclerView, final String category_name,final String total_articles_count) {
        this.context = context;
        this.articles = articles;
        this.category_name=category_name;
        this.total_articles_count=total_articles_count;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            String current_tabTitle = ((CategoryActivity)context).currentTabTitle.trim();

                            if (current_tabTitle.equalsIgnoreCase(category_name) && Integer.parseInt(total_articles_count ) > articles.size() ) {
                                totalItemCount = linearLayoutManager.getItemCount();
                                lastVisibleItem = linearLayoutManager
                                        .findLastVisibleItemPosition();

                                if (!loading
                                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                                    // End has been reached
                                    // Do something
                                    if (onLoadMoreListener != null) {

                                        onLoadMoreListener.onLoadMore();
                                    }
                                    loading = true;
                                }
                            }


                        }
                    });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {


            if (category_name.equalsIgnoreCase("celebrities") || category_name.equalsIgnoreCase("gallery")){
                View gallery_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_gallery, parent, false);
                return new ArticleViewHolder(gallery_view);
            }
            else {
                View category_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category, parent, false);
                return new ArticleViewHolder(category_view);
            }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            Article a = articles.get(position);
            if (category_name.equalsIgnoreCase("celebrities") || category_name.equalsIgnoreCase("gallery")){
                grid_text.setText(a.getTitle());
                Glide.with(this.context).load(a.getBannerMedia()).into(grid_image);
            }
            else{
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

                article_title.setText(a.getTitle());
                Glide.with(this.context).load(a.getBannerMedia()).into(cover_image);
                time_ago.setText(Time);
                comments_count.setText(comment_text);
                likes_count.setText(like_text);
            }
    }
    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    @Override
    public int getItemViewType(int position)
    {
        if (position % 5 == 0){
            return AD_TYPE;}else {
            return CONTENT_TYPE;
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public ArticleViewHolder(View view) {
            super(view);
            if (category_name.equalsIgnoreCase("celebrities") || category_name.equalsIgnoreCase("gallery")){
                grid_text = (TextView) view.findViewById(R.id.grid_text);
                grid_image = (ImageView) view.findViewById(R.id.grid_image);
            } else {
                article_title = (TextView) view.findViewById(R.id.wood_name_sillymonks);
                cover_image = (ImageView) view.findViewById(R.id.wood_cover_image);
                time_ago = (TextView) view.findViewById(R.id.time_ago);
                comments_count =(TextView)view.findViewById(R.id.comments_count);
                likes_count  =(TextView)view.findViewById(R.id.likes_count);
            }
            this.setIsRecyclable(false);

        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.setIsRecyclable(false);

        }
    }

}

