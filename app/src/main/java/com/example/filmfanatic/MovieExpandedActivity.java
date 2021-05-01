package com.example.filmfanatic;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
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


public class MovieExpandedActivity extends YouTubeBaseActivity {        //this not extending appcompat prob is the issue

    //api key and url here
    public static final String YOUTUBE_API_KEY="AIzaSyChn4bC3zVTpy0TYbJQl3FwvcDB7d8p5JA";
    public static final String VIDEOS_URL="https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG="MovieExpandedActivity";



    TextView tvTitle;
    TextView tvDescription;
    YouTubePlayerView youTubePlayerView;
    Button AddtoWishList;
    String youtubeKey;
    Button btnSeeReviews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_expanded);

        tvTitle=findViewById(R.id.tvTitleIntent);
        tvDescription=findViewById(R.id.tvDescriptionIntent);
        youTubePlayerView=findViewById(R.id.playerWLActivity);
        AddtoWishList=findViewById(R.id.btnAddtoWishList);
        btnSeeReviews=findViewById(R.id.btnFanReviews);


        Movie movie= Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvDescription.setText("OVERVIEW: " + movie.getOverview());




        btnSeeReviews.setOnClickListener(new View.OnClickListener() {       //trying this-could try using interface from mainactivity if
            @Override                                                       //this keeps failing
            public void onClick(View v) {

                Intent i = new Intent(MovieExpandedActivity.this, MovieReviewActivity.class);
                i.putExtra("MovieToReview", Parcels.wrap(movie));
                MovieExpandedActivity.this.startActivity(i);

            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
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
                film.saveInBackground(new SaveCallback() {          //saving to cloud-I guess this does it automatically??
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
