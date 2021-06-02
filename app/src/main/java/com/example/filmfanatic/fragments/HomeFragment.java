package com.example.filmfanatic.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.filmfanatic.Film;
import com.example.filmfanatic.FilmAdapter;
import com.example.filmfanatic.MainActivity;
import com.example.filmfanatic.Movie;
import com.example.filmfanatic.MovieExpandedActivity;
import com.example.filmfanatic.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class HomeFragment extends Fragment  {


    public static String NOW_PLAYING_URL;
    public static final String TAG="HomeFragment";

    private List<Movie> MovieList;
    private RecyclerView rvMovies;
    private ContentLoadingProgressBar progressBar;

    public static int WishListCount;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NOW_PLAYING_URL="https://api.themoviedb.org/3/movie/now_playing?api_key=" + getResources().getString(R.string.MovieDBAPIKey);
        rvMovies=view.findViewById(R.id.rvEachMovie);
        MovieList=new ArrayList<>();
        progressBar=view.findViewById(R.id.progress);
        progressBar.show();

        final FilmAdapter filmAdapter=new FilmAdapter(this.getContext(),MovieList);
        rvMovies.setAdapter(filmAdapter);
        rvMovies.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        AsyncHttpClient client=new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {

                progressBar.hide();
                Log.d(TAG,"onSuccess");
                JSONObject jsonObject=json.jsonObject;
                //movies will be stored in array
                try {
                    JSONArray results = jsonObject.getJSONArray("results");      //gets all movies from results section in JSON
                    Log.i(TAG,"Results: " + results.toString());
                    MovieList.addAll(Movie.fromJsonArray(results));        //adding all movies and notifying. Movies created here
                    filmAdapter.notifyDataSetChanged();            //fromJsonArray defined in Movie.java
                    Log.i(TAG, "Movies: " + MovieList.size());
                } catch (JSONException e) {
                    Log.e(TAG,"Hit json exception");
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG,"onFailure");
            }
        });

        ParseQuery<Film> query=ParseQuery.getQuery(Film.class);     //getting wishlist count to determine which layout should be used for
        query.include(Film.KEY_USER);                               //wishlist fragment
        query.whereEqualTo(Film.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Film.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Film>() {
            @Override
            public void done(List<Film> thefilms, ParseException e) {

                WishListCount=thefilms.size();

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}