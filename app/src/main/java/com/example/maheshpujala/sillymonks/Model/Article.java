package com.example.maheshpujala.sillymonks.Model;


import java.io.Serializable;

/**
 * Created by maheshpujala on 3/10/16.
 */

public class Article implements Serializable {

    private String id,title,bannerMedia,publishedAt,likesCount,commentsCount;


    public Article(String id,String title,String bannerMedia,String publishedAt,String likesCount,String commentsCount) {
        this.id = id;
        this.title = title;
        this.bannerMedia = bannerMedia;
        this.publishedAt = publishedAt;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;

    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBannerMedia() {
        return bannerMedia;
    }

    public String getpublishedAt() {
        return publishedAt;
    }

    public String getlikesCount() {
        return likesCount;
    }

    public String getcommentsCount() {
        return commentsCount;
    }


    @Override
    public String toString(){
        return "Title:"+this.title+", Id:"+this.id+", bannermedia:"+this.bannerMedia;
    }


}
