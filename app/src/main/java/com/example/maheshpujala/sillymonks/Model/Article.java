package com.example.maheshpujala.sillymonks.Model;


import java.io.Serializable;

/**
 * Created by maheshpujala on 3/10/16.
 */

public class Article implements Serializable {

    private String id;
    private String title;
    private String bannerMedia;




    public Article(String id,String title,String bannerMedia) {
        this.id = id;
        this.title = title;
        this.bannerMedia = bannerMedia;
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

    @Override
    public String toString(){
        return "Title:"+this.title+", Id:"+this.id+", bannermedia:"+this.bannerMedia;
    }


}
