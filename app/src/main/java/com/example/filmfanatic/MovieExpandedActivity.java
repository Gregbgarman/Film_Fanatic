package com.example.filmfanatic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;


import com.example.filmfanatic.fragments.HomeFragment;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MovieExpandedActivity extends YouTubeBaseActivity {


    public static String YOUTUBE_API_KEY;
    public String VIDEOS_URL;
    public static final String TAG="MovieExpandedActivity";
    public String Movie_Cast_Info;


    private TextView tvTitle;
    private TextView tvDescription;
    private YouTubePlayerView youTubePlayerView;
    private Button AddtoWishList;
    private String youtubeKey;
    private Button btnSeeReviews;
    private ImageView DirectorImage;
    private TextView DirectorName;

    private ImageView ivCastMember1,ivCastMember2,ivCastMember3,ivCastMember4,ivCastMember5;
    private TextView tvCastMember1,tvCastMember2,tvCastMember3,tvCastMember4,tvCastMember5;

    private TextView tvMember1Role,tvMember2Role,tvMember3Role,tvMember4Role,tvMember5Role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_expanded);
        YOUTUBE_API_KEY=getResources().getString(R.string.YoutubeAPIKey);
        VIDEOS_URL="https://api.themoviedb.org/3/movie/%d/videos?api_key="+getResources().getString(R.string.MovieDBAPIKey);


        Window window = getWindow();                  //setting status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.coolgreen));

        tvTitle=findViewById(R.id.tvTitleIntent);
        tvDescription=findViewById(R.id.tvDescriptionIntent);
        youTubePlayerView=findViewById(R.id.playerWLActivity);
        AddtoWishList=findViewById(R.id.btnAddtoWishList);
        btnSeeReviews=findViewById(R.id.btnFanReviews);
        DirectorImage=findViewById(R.id.ivDirectorImage);
        DirectorImage.setImageResource(R.drawable.popcorn);
        DirectorName=findViewById(R.id.tvDirectorName);

        ivCastMember1=findViewById(R.id.ivCastMember1);         //setting default images here (wouldnt work doing in xml for some reason)
        ivCastMember1.setImageResource(R.drawable.popcorn);
        ivCastMember2=findViewById(R.id.ivCastMember2);
        ivCastMember2.setImageResource(R.drawable.popcorn);
        ivCastMember3=findViewById(R.id.ivCastMember3);
        ivCastMember3.setImageResource(R.drawable.popcorn);
        ivCastMember4=findViewById(R.id.ivCastMember4);
        ivCastMember4.setImageResource(R.drawable.popcorn);
        ivCastMember5=findViewById(R.id.ivCastMember5);
        ivCastMember5.setImageResource(R.drawable.popcorn);

        tvCastMember1=findViewById(R.id.tvCastMember1);
        tvCastMember2=findViewById(R.id.tvCastMember2);
        tvCastMember3=findViewById(R.id.tvCastMember3);
        tvCastMember4=findViewById(R.id.tvCastMember4);
        tvCastMember5=findViewById(R.id.tvCastMember5);

        tvMember1Role=findViewById(R.id.tvCastMember1Role);
        tvMember2Role=findViewById(R.id.tvCastMember2Role);
        tvMember3Role=findViewById(R.id.tvCastMember3Role);
        tvMember4Role=findViewById(R.id.tvCastMember4Role);
        tvMember5Role=findViewById(R.id.tvCastMember5Role);


        Movie movie= Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvDescription.setText("OVERVIEW: " + movie.getOverview());
        Movie_Cast_Info="https://api.themoviedb.org/3/movie/" + movie.getMovieId() +"/credits?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        btnSeeReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MovieExpandedActivity.this, MovieReviewActivity.class);
                i.putExtra("MovieToReview", Parcels.wrap(movie));
                MovieExpandedActivity.this.startActivity(i);

            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {   //getting youtube player
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results=json.jsonObject.getJSONArray("results");
                    if (results.length()==0) {
                        return;
                    }
                    youtubeKey=results.getJSONObject(0).getString("key");
                    Log.d(TAG,youtubeKey);
                    initializeYoutube(youtubeKey);
                } catch (JSONException e) {
                    Log.e(TAG,"Failed to parse JSON");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG,"Failed to load json");
            }
        });

        AsyncHttpClient client2 = new AsyncHttpClient();
        client2.get(Movie_Cast_Info, new JsonHttpResponseHandler() {        //getting movie cast info
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {

                try {
                    JSONArray CastResults=json.jsonObject.getJSONArray("cast");
                    JSONArray CrewResults=json.jsonObject.getJSONArray("crew");
                    if (CrewResults.length()==0) {
                        return;
                    }

                    for (int j=0;j<CrewResults.length();j++){                              //getting director name and profile path
                        if (CrewResults.getJSONObject(j).get("job").equals("Director")){
                                DirectorName.setText(CrewResults.getJSONObject(j).get("name").toString());
                                String ImagePath=CrewResults.getJSONObject(j).get("profile_path").toString();
                                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+ImagePath).into(DirectorImage);


                        }

                    }

                    if (CastResults.length()==0){
                        return;
                    }

                    for (int j=0;j<5;j++){                              //getting 5 cast members and profile paths
                        if (j==0){
                            tvCastMember1.setText(CastResults.getJSONObject(j).get("name").toString());
                            tvMember1Role.setText(CastResults.getJSONObject(j).get("character").toString());
                            String ImagePath=CastResults.getJSONObject(j).get("profile_path").toString();
                            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+ImagePath).into(ivCastMember1);

                        }

                        else if (j==1){
                            tvCastMember2.setText(CastResults.getJSONObject(j).get("name").toString());
                            tvMember2Role.setText(CastResults.getJSONObject(j).get("character").toString());
                            String ImagePath=CastResults.getJSONObject(j).get("profile_path").toString();
                            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+ImagePath).into(ivCastMember2);
                        }

                        else if (j==2){
                            tvCastMember3.setText(CastResults.getJSONObject(j).get("name").toString());
                            tvMember3Role.setText(CastResults.getJSONObject(j).get("character").toString());
                            String ImagePath=CastResults.getJSONObject(j).get("profile_path").toString();
                            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+ImagePath).into(ivCastMember3);
                        }
                        else if (j==3){
                            tvCastMember4.setText(CastResults.getJSONObject(j).get("name").toString());
                            tvMember4Role.setText(CastResults.getJSONObject(j).get("character").toString());
                            String ImagePath=CastResults.getJSONObject(j).get("profile_path").toString();
                            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+ImagePath).into(ivCastMember4);
                        }
                        else if (j==4){
                            tvCastMember5.setText(CastResults.getJSONObject(j).get("name").toString());
                            tvMember5Role.setText(CastResults.getJSONObject(j).get("character").toString());
                            String ImagePath=CastResults.getJSONObject(j).get("profile_path").toString();
                            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+ImagePath).into(ivCastMember5);
                        }


                    }



                } catch (JSONException e) {
                    Log.e(TAG,"Failed to parse CastInfo JSON");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG,"Failed to load json");
            }
        });





        AddtoWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Film film=new Film();
                film.setFilmTitle(movie.getTitle().toString());
                film.setFilmDescription(movie.getOverview().toString());
                film.setFilmPoster(movie.getPosterPath().toString());
                film.setFilmRating( movie.getRating() );
                film.setUser(ParseUser.getCurrentUser());
                film.setFilmVideo(youtubeKey);
                film.setFilmLargePoster(movie.getBackdropPath());
                film.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!=null){
                           Toast.makeText(getApplicationContext(), "Error while saving", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Movie Added to Wishlist", Toast.LENGTH_SHORT).show();
                            HomeFragment.WishListCount++;
                        }
                    }
                });


            }
        });
    }

    private void initializeYoutube(final String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onSuccess");
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onFailure");
            }
        });
    }





}
