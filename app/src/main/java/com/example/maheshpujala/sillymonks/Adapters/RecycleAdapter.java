package com.example.maheshpujala.sillymonks.Adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by maheshpujala on 27/9/16.
 */
public class RecycleAdapter extends RecyclerView.Adapter {
    private  List<String> title;
    private final Activity context;
    private  List<String> image;
    private final int responseCode;
    private String[] imageArray;
    List<Article> articles;

    TextView article_title,grid_text;
    ImageView cover_image,grid_image;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Article> articleList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public RecycleAdapter(Activity context, List<String> title, List<String> image, int responseCode) {
        this.context = context;
        this.title = title;
        this.image=image;
        this.responseCode=responseCode;

        for(int i=0;i<=image.size();i++) {
            imageArray = new String[image.size()];
            imageArray = image.toArray(imageArray);
        }
        Log.e("Recycler Adapter", "Entered");
    }

    public RecycleAdapter(Activity context, List<Article> articles, int responseCode,RecyclerView recyclerView) {
        this.context = context;
        this.articles = articles;
        this.responseCode=responseCode;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

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
                    });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return articleList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if (responseCode == 1) {
            if (viewType == VIEW_ITEM) {
                View article_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category, parent, false);

                vh = new ArticleViewHolder(article_view);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
    }
            else {
            View gallery_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_gallery, parent, false);
            return new ArticleViewHolder(gallery_view);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("Recycler Adapter", "onBindViewHolder");
        if (responseCode == 1) {
            if (holder instanceof ArticleViewHolder) {
                Article a = articles.get(position);
                article_title.setText(a.getId() + " - " + a.getTitle());
                Picasso.with(this.context).load(a.getBannerMedia()).fit().into(cover_image);

            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        }
            else{
            grid_text.setText(title.get(position));
            //  grid_image.setImageResource(image[position]);
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

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public ArticleViewHolder(View view) {
            super(view);
            Log.e("Recycler Adapter", "ViewHolder");
            if (responseCode == 1) {
                article_title = (TextView) view.findViewById(R.id.wood_name_sillymonks);
                cover_image = (ImageView) view.findViewById(R.id.wood_cover_image);
            } else {
                grid_text = (TextView) view.findViewById(R.id.grid_text);
                grid_image = (ImageView) view.findViewById(R.id.grid_image);
            }
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}

//
//===================
//        package com.example.maheshpujala.sillymonks.Adapters;
//
//        import android.app.Activity;
//        import android.support.v7.widget.LinearLayoutManager;
//        import android.support.v7.widget.RecyclerView;
//        import android.util.Log;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.ImageView;
//        import android.widget.ProgressBar;
//        import android.widget.TextView;
//
//        import com.example.maheshpujala.sillymonks.Model.Article;
//        import com.example.maheshpujala.sillymonks.R;
//        import com.squareup.picasso.Picasso;
//
//        import java.util.List;
//
//
///**
// * Created by maheshpujala on 27/9/16.
// */
//public class RecyclerAdapter extends RecyclerView.Adapter {
//    private  List<String> title;
//    private final Activity context;
//    private  List<String> image;
//    private final int responseCode;
//    private String[] imageArray;
//    List<Article> articles;
//
//    TextView article_title,grid_text;
//    ImageView cover_image,grid_image;
//
//
//    public RecyclerAdapter(Activity context, List<String> title, List<String> image, int responseCode) {
//        this.context = context;
//        this.title = title;
//        this.image=image;
//        this.responseCode=responseCode;
//
//        for(int i=0;i<=image.size();i++) {
//            imageArray = new String[image.size()];
//            imageArray = image.toArray(imageArray);
//        }
//        Log.e("Recycler Adapter", "Entered");
//    }
//
//    public RecyclerAdapter(Activity context, List<Article> articles, int responseCode,RecyclerView recyclerView) {
//        this.context = context;
//        this.articles = articles;
//        this.responseCode=responseCode;
//
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
//        RecyclerView.ViewHolder vh;
//        if (responseCode == 1) {
//            View article_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category, parent, false);
//
//            vh = new ArticleViewHolder(article_view);
//        }else {
//            View gallery_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_gallery, parent, false);
//            return new ArticleViewHolder(gallery_view);
//        }
//        return vh;
//    }
//
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Log.e("Recycler Adapter", "onBindViewHolder");
//        if (responseCode == 1) {
//            Article a = articles.get(position);
//            article_title.setText(a.getId()+" - "+a.getTitle());
//            Picasso.with(this.context).load(a.getBannerMedia()).fit().into(cover_image);
//
//        }else{
//            grid_text.setText(title.get(position));
//            //  grid_image.setImageResource(image[position]);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return articles.size();
//    }
//
//    public class ArticleViewHolder extends RecyclerView.ViewHolder {
//        public ArticleViewHolder(View view) {
//            super(view);
//            Log.e("Recycler Adapter", "ViewHolder");
//            if (responseCode == 1) {
//                article_title = (TextView) view.findViewById(R.id.wood_name_sillymonks);
//                cover_image = (ImageView) view.findViewById(R.id.wood_cover_image);
//            } else {
//                grid_text = (TextView) view.findViewById(R.id.grid_text);
//                grid_image = (ImageView) view.findViewById(R.id.grid_image);
//            }
//        }
//    }
//
//}
