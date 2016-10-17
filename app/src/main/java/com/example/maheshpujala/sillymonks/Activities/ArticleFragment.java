package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ArticleFragment extends Fragment implements View.OnClickListener {

    RatingBar rating;
    ImageView comment, share, like;
    String articlePageURL, categoryName, categoryID, wood_id, articleID, id, title, category_name_fromJson, likes, banner_image, youtube_id, brightcove_link, description, related_article_id, related_article_title, related_article_image, relatedArticleId;
    TextView text_heading, article_title, article_description, movie_name1, movie_name2, movie_name3, movie_name4, more_articles;
    ImageView article_banner, play_image, hztl_image1, hztl_image2, hztl_image3, hztl_image4;
    FlexboxLayout layout_tags;
    List<String> tags, relatedArticlesId, relatedArticlesTitle, relatedArticlesImage;
    FlexboxLayout.LayoutParams params;
    List<Article> relatedArticlesList,articlesList;
    RelativeLayout layout_related_articles;

    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private AdChoicesView adChoicesView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle  arguments = this.getArguments();
        articleID = arguments.getString("articleID");
        categoryID = arguments.getString("categoryID");
        wood_id = arguments.getString("wood_id");
        categoryName = arguments.getString("categoryName");
        articlesList = (List<Article>) arguments.getSerializable("articles");

        sendRequest(articleID,categoryID,wood_id);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_article, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        nativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);

        article_banner = (ImageView) view.findViewById(R.id.article_banner);
        article_title = (TextView) view.findViewById(R.id.article_title);
        text_heading = (TextView) view.findViewById(R.id.text_heading);
        layout_related_articles = (RelativeLayout) view.findViewById(R.id.layout_related_articles);
        article_description = (TextView) view.findViewById(R.id.article_description);
        layout_tags = (FlexboxLayout) view.findViewById(R.id.layout_tags);
        params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 5, 8, 5);
        if (categoryName.equalsIgnoreCase("Celebrities")) {
            text_heading.setText("Related Celebrities");
        }else{
            text_heading.setText("Related Articles");
        }

        play_image = (ImageView) view.findViewById(R.id.play_image);
        play_image.setOnClickListener(this);

        rating = (RatingBar) view.findViewById(R.id.ratingBar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), Float.toString(rating), Toast.LENGTH_LONG).show();
            }

        });

        comment = (ImageView) view.findViewById(R.id.comment);
        share = (ImageView) view.findViewById(R.id.share);
        like = (ImageView) view.findViewById(R.id.favourite_heart);
        hztl_image1 = (ImageView) view.findViewById(R.id.hztl_image1);
        hztl_image2 = (ImageView) view.findViewById(R.id.hztl_image2);
        hztl_image3 = (ImageView) view.findViewById(R.id.hztl_image3);
        hztl_image4 = (ImageView) view.findViewById(R.id.hztl_image4);
        movie_name1 = (TextView) view.findViewById(R.id.movie_name1);
        movie_name2 = (TextView) view.findViewById(R.id.movie_name2);
        movie_name3 = (TextView) view.findViewById(R.id.movie_name3);
        movie_name4 = (TextView) view.findViewById(R.id.movie_name4);
        more_articles = (TextView) view.findViewById(R.id.more_articles);
        hztl_image1.setOnClickListener(this);
        hztl_image2.setOnClickListener(this);
        hztl_image3.setOnClickListener(this);
        hztl_image4.setOnClickListener(this);
        movie_name1.setOnClickListener(this);
        movie_name2.setOnClickListener(this);
        movie_name3.setOnClickListener(this);
        movie_name4.setOnClickListener(this);
        more_articles.setOnClickListener(this);
        showNativeAd();
    }

    private void showNativeAd() {
        nativeAd = new NativeAd(getContext(), "948537241887382_1177898238951280");
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                Log.e("getErrorMessage()==="+error.getErrorMessage(),"AD getErrorCode==="+error.getErrorCode());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (ad != nativeAd) {
                    return;
                }

                // Add ad into the ad container.
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                adView = (LinearLayout) inflater.inflate(R.layout.fb_ad_unit, nativeAdContainer, false);
                nativeAdContainer.addView(adView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);
                // Setting the Text.
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdBody.setText(nativeAd.getAdBody());

                // Downloading and setting the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and setting the cover image.
                nativeAdMedia.setNativeAd(nativeAd);

                // Add adChoices icon
                if (adChoicesView == null) {
                    adChoicesView = new AdChoicesView(getApplicationContext(), nativeAd, true);
                    adView.addView(adChoicesView, 0);
                }

                nativeAd.registerViewForInteraction(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        });

        nativeAd.loadAd();
    }

// The next step is to extract the ad metadata and use its properties
// to build your customized native UI. Modify the onAdLoaded function
// above to retrieve the ad properties. For example:

    public void sendRequest(String articleId, String categoryId, String wood_id) {
        if (categoryName.equalsIgnoreCase("Celebrities")) {

            articlePageURL = getResources().getString(R.string.main_url) + getResources().getString(R.string.celebrity_url) + articleId + getResources().getString(R.string.os_tag);
        } else {

            articlePageURL = getResources().getString(R.string.main_url) + getResources().getString(R.string.article_page_url) + articleId + getResources().getString(R.string.os_tag);
        }
        final String relatedArticlesURL = getResources().getString(R.string.main_url) + getResources().getString(R.string.related_articles_url) + articleId + getResources().getString(R.string.categoryId_url) + categoryId + getResources().getString(R.string.wood_id_url) + wood_id;
        Log.e("relatedArticlesURL", relatedArticlesURL);
// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, articlePageURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getData(response);
                        getRelatedArticles(relatedArticlesURL);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        reportError(error);
                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void getRelatedArticles(String relatedArticlesUrl) {
        // Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, relatedArticlesUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getRelatedData(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        reportError(error);
                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void getRelatedData(JSONObject response) {
        try {
            JSONArray related_articles_array = response.getJSONArray("articles");
//            relatedArticlesId = new ArrayList<String>();
//            relatedArticlesTitle = new ArrayList<String>();
//            relatedArticlesImage = new ArrayList<String>();

            relatedArticlesList = new ArrayList<Article>();

            for (int k = 0; k < related_articles_array.length(); k++) {
                JSONObject relatedArticles = related_articles_array.getJSONObject(k);

                relatedArticlesList.add(new Article(relatedArticles.getString("id"),
                        relatedArticles.getString("title"),
                        relatedArticles.getString("large"),
                        relatedArticles.getString("published_at"),
                        relatedArticles.getString("likes_count"),
                        relatedArticles.getString("comments_count")));

//                related_article_id = relatedArticles.getString("id");
//                related_article_title = relatedArticles.getString("title");
//                related_article_image = relatedArticles.getString("original");
//
//                relatedArticlesId.add(related_article_id);
//                relatedArticlesTitle.add(related_article_title);
//                relatedArticlesImage.add(related_article_image);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(relatedArticlesList.size()>=4){
            setRelatedArticles();
        }else{
            layout_related_articles.setVisibility(View.GONE);
        }
    }

    private void setRelatedArticles() {

        Picasso.with(getContext()).load(relatedArticlesList.get(0).getBannerMedia()).into(hztl_image1);
        Picasso.with(getContext()).load(relatedArticlesList.get(1).getBannerMedia()).into(hztl_image2);
        Picasso.with(getContext()).load(relatedArticlesList.get(2).getBannerMedia()).into(hztl_image3);
        Picasso.with(getContext()).load(relatedArticlesList.get(3).getBannerMedia()).into(hztl_image4);
        movie_name1.setText(relatedArticlesList.get(0).getTitle());
        movie_name2.setText(relatedArticlesList.get(1).getTitle());
        movie_name3.setText(relatedArticlesList.get(2).getTitle());
        movie_name4.setText(relatedArticlesList.get(3).getTitle());
//        Picasso.with(getContext()).load(relatedArticlesImage.get(0)).into(hztl_image1);
//        Picasso.with(getContext()).load(relatedArticlesImage.get(1)).into(hztl_image2);
//        Picasso.with(getContext()).load(relatedArticlesImage.get(2)).into(hztl_image3);
//        Picasso.with(getContext()).load(relatedArticlesImage.get(3)).into(hztl_image4);
//        movie_name1.setText(relatedArticlesTitle.get(0));
//        movie_name2.setText(relatedArticlesTitle.get(1));
//        movie_name3.setText(relatedArticlesTitle.get(2));
//        movie_name4.setText(relatedArticlesTitle.get(3));
    }


    public void getData(JSONObject json) {
        try {
            if (categoryName.equalsIgnoreCase("celebrities")) {
                JSONObject celebrity_data = json.getJSONObject("celebrity");
                id = celebrity_data.getString("id");
                title = celebrity_data.getString("name");
                category_name_fromJson = celebrity_data.getString("category_name");
                description = celebrity_data.getString("profile");
                banner_image = celebrity_data.getString("original");
            } else {
                JSONObject article_data = json.getJSONObject("article");
                id = article_data.getString("id");
                title = article_data.getString("title");
                category_name_fromJson = article_data.getString("category_name");
                likes = article_data.getString("likes_count");
                banner_image = article_data.getString("original");
                youtube_id = article_data.getString("youtube_link");
                brightcove_link = article_data.getString("brightcove_link");
                description = article_data.getString("description");
                JSONArray articleTags = article_data.getJSONArray("tags");

                tags = new ArrayList<String>();

                for (int k = 0; k < articleTags.length(); k++) {
                    tags.add(articleTags.get(k).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setDataInViews();
    }

    private void setDataInViews() {

        Picasso.with(getContext()).load(banner_image).into(article_banner);
        article_title.setText(title);
        article_description.setText(Html.fromHtml(description));

        if (categoryName.equalsIgnoreCase("celebrities")) {
        } else {
            if (youtube_id.length() > 0) {
                play_image.setVisibility(View.VISIBLE);
            }

            setTags();
        }


    }

    private void setTags() {
        if (!tags.isEmpty()) {
            final TextView tag[] = new TextView[tags.size()];
            for (int i = 0; i < tags.size(); i++) {
                tag[i] = new TextView(getContext());
                tag[i].setLayoutParams(params);
                tag[i].setText("# " + tags.get(i));
                tag[i].setTextColor(ContextCompat.getColor(getContext(), R.color.tag_text_color));
                tag[i].setBackgroundResource(R.drawable.tags_background);
                tag[i].setPadding(5, 5, 5, 5);
                layout_tags.addView(tag[i]);
                final int finalI = i;
                tag[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "clicked" + tag[finalI].getText(), Toast.LENGTH_SHORT).show();
                        Intent searchResult = new Intent(getContext(),RelatedArticles.class);
                        searchResult.putExtra("identifyActivity","SearchResult");
                        String query = tag[finalI].getText().toString().substring(2);
                        searchResult.putExtra("searchQuery",query);
                        startActivity(searchResult);
                    }
                });
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.play_image) {
            Intent youtubeActivity = new Intent(getContext(), YoutubeActivity.class);
            youtubeActivity.putExtra("YoutubeId", youtube_id);
            startActivity(youtubeActivity);
        }
        if (id == R.id.hztl_image1 || id == R.id.movie_name1) {
            relatedArticleId = relatedArticlesList.get(0).getId();
            Log.e("relatedArticleId", "" + relatedArticleId);
            relatedArticleClick(relatedArticleId,0);


        }
        if (id == R.id.hztl_image2 || id == R.id.movie_name2) {
            relatedArticleId = relatedArticlesList.get(1).getId();
            Log.e("relatedArticleId", "" + relatedArticleId);
            relatedArticleClick(relatedArticleId,1);

        }
        if (id == R.id.hztl_image3 || id == R.id.movie_name3) {
            relatedArticleId = relatedArticlesList.get(2).getId();
            Log.e("relatedArticleId", "" + relatedArticleId);
            relatedArticleClick(relatedArticleId,2);

        }
        if (id == R.id.hztl_image4 || id == R.id.movie_name4) {
            relatedArticleId = relatedArticlesList.get(3).getId();
            Log.e("relatedArticleId", "" + relatedArticleId);
            relatedArticleClick(relatedArticleId,3);

        }
        if (id == R.id.more_articles) {
            getActivity().finish();
            Intent moreArticles = new Intent(getContext(), RelatedArticles.class);
            moreArticles.putExtra("identifyActivity", "RelatedArticles");
            moreArticles.putExtra("categoryID", categoryID);
            moreArticles.putExtra("articleID", articleID);
            moreArticles.putExtra("categoryName", category_name_fromJson);
            moreArticles.putExtra("wood_id", wood_id);
            Log.e("FROM ARTICLE ACTIVYT" + "articleID=" + articleID + "categoryID=" + categoryID, "categoryName=" + categoryName + "wood_id=" + wood_id);

            startActivity(moreArticles);

        }
    }

    private void relatedArticleClick(String relatedArticleId,int selected_position) {
        getActivity().finish();
        Intent articleScreen = new Intent(getContext(), ArticleActivity.class);
        articleScreen.putExtra("articles", (Serializable) relatedArticlesList);
        articleScreen.putExtra("previousArticles", (Serializable) articlesList);
        articleScreen.putExtra("articleID", relatedArticleId);
        articleScreen.putExtra("categoryID", categoryID);
        articleScreen.putExtra("selected_position",""+selected_position);
        if (categoryName.equalsIgnoreCase("celebrities")) {
            articleScreen.putExtra("categoryName", categoryName);
        } else {
            articleScreen.putExtra("categoryName", category_name_fromJson);
        }
        articleScreen.putExtra("wood_id", wood_id);
        startActivity(articleScreen);
    }

    private void reportError(VolleyError error) {
        Log.e("response Errorhome", error + "");
        if (error instanceof NoConnectionError) {
            Log.d("NoConnectionError>>>>>>>>>", "NoConnectionError.......");
        } else if (error instanceof AuthFailureError) {
            Log.d("AuthFailureError>>>>>>>>>", "AuthFailureError.......");
        } else if (error instanceof ServerError) {
            Log.d("ServerError>>>>>>>>>", "ServerError.......");
        } else if (error instanceof NetworkError) {
            Log.d("NetworkError>>>>>>>>>", "NetworkError.......");
        } else if (error instanceof ParseError) {
            Log.d("ParseError>>>>>>>>>", "ParseError.......");
        } else if (error instanceof TimeoutError) {
            Log.d("TimeoutError>>>>>>>>>", "TimeoutError.......");
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.myDialog));

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Unable to connect with the server.Try again after some time.")
                    .setTitle("Server Error");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }
}
