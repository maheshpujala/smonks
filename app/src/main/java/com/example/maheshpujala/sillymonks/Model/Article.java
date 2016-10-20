package com.example.maheshpujala.sillymonks.Model;


import java.io.Serializable;

/**
 * Created by maheshpujala on 3/10/16.
 */

public class Article implements Serializable {

    private String id,title,bannerMedia,publishedAt,likesCount,commentsCount,first_cat_id,first_cat_name,first_wood_id,thumb_image,description;

    public Article(String aid,String cat_id,String cat_name,String wood_id,String Atitle,String image,String description) {
        this.id = aid;
        this.first_cat_id = cat_id;
        this.first_cat_name = cat_name;
        this.first_wood_id = wood_id;
        this.title = Atitle;
        this.thumb_image = image;
        this.description=description;
    }

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

    public String getFirstCatId() {
        return first_cat_id;
    }

    public String getFirstCatName() {
        return first_cat_name;
    }

    public String getFirstWoodId() {
        return first_wood_id;
    }

    public String getThumbImage() {
        return thumb_image;
    }

    public String getDescription() {
        return description;
    }

    public  void  setFirstCategoryName(String first_cat_name){
        this.first_cat_name = first_cat_name;
    }


    @Override
    public String toString(){
        return "Title:"+this.title+", Id:"+this.id+", bannermedia:"+this.bannerMedia+
                ", publishedAt:"+this.publishedAt+", likesCount:"+this.likesCount+
                ", commentCount:"+this.commentsCount+", FirstCatId:"+this.first_cat_id+
                ", FirstCatName:"+this.first_cat_name+", FirstWoodId:"+this.first_wood_id+
                ", ThumbImage:"+this.thumb_image+", Description:"+this.description;
    }


}
