package com.example.maheshpujala.sillymonks.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.bumptech.glide.Glide;
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.login.LoginManager;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.auth.api.Auth;

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
    String articlePageURL, categoryName, categoryID, wood_id, articleID, id, title, category_name_fromJson, likes, banner_image, youtube_id, brightcove_link, description,comments,related_article_id, related_article_title, related_article_image, relatedArticleId;
    Float average_rating;
    TextView text_heading, article_title, article_description,text_like, movie_name1, movie_name2, movie_name3, movie_name4, more_articles,text_rating;
    ImageView article_banner, play_image, hztl_image1, hztl_image2, hztl_image3, hztl_image4;
    FlexboxLayout layout_tags;
    List<String> tags, relatedArticlesId, relatedArticlesTitle, relatedArticlesImage;
    FlexboxLayout.LayoutParams params;
    List<Article> relatedArticlesList,articlesList;
    RelativeLayout layout_related_articles;
    boolean isLikedByUser;
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private AdChoicesView adChoicesView;
    SessionManager session;
    List<UserData> userData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle  arguments = this.getArguments();
        articleID = arguments.getString("articleID");
        categoryID = arguments.getString("categoryID");
        wood_id = arguments.getString("wood_id");
        categoryName = arguments.getString("categoryName");
        articlesList = (List<Article>) arguments.getSerializable("articles");

        session = new SessionManager(getContext());
        userData = session.getUserDetails();

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
        text_heading.setText("Related Articles");

        play_image = (ImageView) view.findViewById(R.id.play_image);
        play_image.setOnClickListener(this);

        rating = (RatingBar) view.findViewById(R.id.ratingBar);
        rating.setOnTouchListener(new View.OnTouchListener() {
                                      @Override
                                      public boolean onTouch(View v, MotionEvent event) {
                                          if (event.getAction() == MotionEvent.ACTION_UP) {
                                              // TODO perform your action here
                                              Log.e("setOnTouchListener","RATING BAR");
                                              showRatingDialog();
                                          }
                                          return true;
                                      }
                                  });

        comment = (ImageView) view.findViewById(R.id.comment);
        share = (ImageView) view.findViewById(R.id.share);
        like = (ImageView) view.findViewById(R.id.favourite_heart);
        text_like = (TextView) view.findViewById(R.id.text_like);
        text_rating = (TextView) view.findViewById(R.id.text_rating);
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
        like.setOnClickListener(this);
        share.setOnClickListener(this);
        comment.setOnClickListener(this);
        showNativeAd();
    }

    private void showNativeAd() {
        AdSettings.addTestDevice("74c31e936f85fe7dae895251d175f7cc");
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

    public void sendRequest(final String articleId, String categoryId, String wood_id) {
        articlePageURL = getResources().getString(R.string.main_url) + getResources().getString(R.string.article_page_url) + articleId + getResources().getString(R.string.os_tag);
        final String relatedArticlesURL = getResources().getString(R.string.main_url) + getResources().getString(R.string.related_articles_url) + articleId + getResources().getString(R.string.categoryId_url) + categoryId + getResources().getString(R.string.wood_id_url) + wood_id;
// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, articlePageURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getData(response);
                        getRelatedArticles(relatedArticlesURL);
                        isLiked(articleId);
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

    public void isLiked(String articleId) {

      final String  isLikedURL = getResources().getString(R.string.main_url) + getResources().getString(R.string.isLiked_url) + articleId + getResources().getString(R.string.userId_url)+userData.get(0).getSmonksId();

        // Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, isLikedURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject likes=response.getJSONObject("likes") ;
                            isLikedByUser = likes.getBoolean("liked");
                            if(isLikedByUser){
                                like.setImageResource(R.drawable.heart);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            relatedArticlesList = new ArrayList<Article>();

            for (int k = 0; k < related_articles_array.length(); k++) {
                JSONObject relatedArticles = related_articles_array.getJSONObject(k);
                Article a = new Article(relatedArticles.getString("id"),
                        relatedArticles.getString("title"),
                        relatedArticles.getString("large"),
                        relatedArticles.getString("published_at"),
                        relatedArticles.getString("likes_count"),
                        relatedArticles.getString("comments_count"));
                a.setFirstCategoryName(relatedArticles.getString("first_cageory_name"));
                relatedArticlesList.add(a);

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

        Glide.with(getContext()).load(relatedArticlesList.get(0).getBannerMedia()).into(hztl_image1);
        Glide.with(getContext()).load(relatedArticlesList.get(1).getBannerMedia()).into(hztl_image2);
        Glide.with(getContext()).load(relatedArticlesList.get(2).getBannerMedia()).into(hztl_image3);
        Glide.with(getContext()).load(relatedArticlesList.get(3).getBannerMedia()).into(hztl_image4);
        movie_name1.setText(relatedArticlesList.get(0).getTitle());
        movie_name2.setText(relatedArticlesList.get(1).getTitle());
        movie_name3.setText(relatedArticlesList.get(2).getTitle());
        movie_name4.setText(relatedArticlesList.get(3).getTitle());
    }

    public void getData(JSONObject json) {
        try {

                JSONObject article_data = json.getJSONObject("article");
            JSONArray articleTags = article_data.getJSONArray("tags");
            tags = new ArrayList<String>();

            for (int k = 0; k < articleTags.length(); k++) {
                tags.add(articleTags.get(k).toString());
            }
                id = article_data.getString("id");
                title = article_data.getString("title");
                category_name_fromJson = article_data.getString("category_name");
                likes = article_data.getString("likes_count");
                banner_image = article_data.getString("original");
                youtube_id = article_data.getString("youtube_link");
                brightcove_link = article_data.getString("brightcove_link");
                description = article_data.getString("description");
                average_rating = Float.valueOf(article_data.getString("average_rating"));
                comments = article_data.getString("comments");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        setDataInViews();
    }

    private void setDataInViews() {

        Glide.with(getContext()).load(banner_image).into(article_banner);
        article_title.setText(title);
        article_description.setText(Html.fromHtml(description));
        text_like.setText("Likes :"+likes);
        if (youtube_id.length() > 0) {
             play_image.setVisibility(View.VISIBLE);
        }
        setTags();
        rating.setRating(average_rating);
        text_rating.setText("Avg. user ratings:"+average_rating+"/5");


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

        if(id == R.id.favourite_heart){
            setLike();
        }

        if(id == R.id.share){
            try{
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "SillyMonks Ride On Digital");
                String sAux = "\nLet me share you this article\n\n";
                sAux = sAux + "http://sillymonksapp.com/share/show_article/"+"wood_name"+"/"+"categoryName"+"/"+"articleID";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if (id == R.id.comment){
        }


        if (id == R.id.play_image) {
            Intent youtubeActivity = new Intent(getContext(), YoutubeActivity.class);
            youtubeActivity.putExtra("YoutubeId", youtube_id);
            startActivity(youtubeActivity);
        }
        if (id == R.id.hztl_image1 || id == R.id.movie_name1) {
            relatedArticleId = relatedArticlesList.get(0).getId();
            relatedArticleClick(relatedArticleId,0);

        }
        if (id == R.id.hztl_image2 || id == R.id.movie_name2) {
            relatedArticleId = relatedArticlesList.get(1).getId();
            relatedArticleClick(relatedArticleId,1);

        }
        if (id == R.id.hztl_image3 || id == R.id.movie_name3) {
            relatedArticleId = relatedArticlesList.get(2).getId();
            relatedArticleClick(relatedArticleId,2);

        }
        if (id == R.id.hztl_image4 || id == R.id.movie_name4) {
            relatedArticleId = relatedArticlesList.get(3).getId();
            relatedArticleClick(relatedArticleId,3);

        }
        if (id == R.id.more_articles) {
            getActivity().finish();
            Intent moreArticles = new Intent(getContext(),RelatedArticles.class);
            moreArticles.putExtra("identifyActivity", "RelatedArticles");
            moreArticles.putExtra("categoryID", categoryID);
            moreArticles.putExtra("articleID", articleID);
            moreArticles.putExtra("categoryName", category_name_fromJson);
            moreArticles.putExtra("wood_id", wood_id);

            startActivity(moreArticles);

        }

    }

    private void showRatingDialog() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext());
        final RatingBar rating = new RatingBar(getContext());
        rating.setMax(5);
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("User Rating ");
        popDialog.setView(rating);

        // Button OK
        popDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                rating.setRating(rating.getProgress());
                dialog.dismiss();
            }
        })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        popDialog.create();
        popDialog.show();
    }


    private void setLike() {
        if(isLikedByUser){
            like.setImageResource(R.drawable.mt_heart);
            isLikedByUser =false;
        }else{
            like.setImageResource(R.drawable.heart);
            isLikedByUser =true;
        }
        sendLike();

    }

    private void sendLike() {
        String sendLikeUrl;
        if(isLikedByUser){
            sendLikeUrl = getResources().getString(R.string.main_url)+getResources().getString(R.string.userLiked_url)+articleID
                    +getResources().getString(R.string.userId_url)+userData.get(0).getSmonksId();
        }else{
            sendLikeUrl = getResources().getString(R.string.main_url)+getResources().getString(R.string.userDisliked_url)+articleID
                    +getResources().getString(R.string.userId_url)+userData.get(0).getSmonksId();
        }

        // Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, sendLikeUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject likes_data = response.getJSONObject("likes");
                            String likesCount = likes_data.getString("likes_count");
                            text_like.setText("Likes :"+likesCount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void relatedArticleClick(String relatedArticleId,int selected_position) {
        getActivity().finish();
        Intent articleScreen = new Intent(getContext(), ArticleActivity.class);
        articleScreen.putExtra("identifyActivity","relatedArticles" );
        articleScreen.putExtra("articles", (Serializable) relatedArticlesList);
        articleScreen.putExtra("previousArticles", (Serializable) articlesList);
        articleScreen.putExtra("articleID", relatedArticleId);
        articleScreen.putExtra("categoryID", categoryID);
        articleScreen.putExtra("selected_position",""+selected_position);
//        articleScreen.putExtra("firstCategoryName", relatedArticlesList.get(selected_position).getFirstCatName());
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
