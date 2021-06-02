package com.example.filmfanatic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filmfanatic.fragments.HomeFragment;
import com.example.filmfanatic.fragments.WishListFragment;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class WishListActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvRating;
    private Button RemoveFilm;

    public static final String TAG="WLActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        youTubePlayerView=findViewById(R.id.playerWLActivity);
        tvTitle=findViewById(R.id.tvTitleAWL);
        tvDescription=findViewById(R.id.tvDescriptionAWL);
        tvRating=findViewById(R.id.tvRatingAWL);
        RemoveFilm=findViewById(R.id.btnRemove);

        Film film= Parcels.unwrap(getIntent().getParcelableExtra("film"));

        tvTitle.setText(film.getFilmTitle());
        tvDescription.setText("OVERVIEW: " + film.getFilmDescription());
        Float f=(float)film.getFilmRating()/2;
        tvRating.setText(f.toString() + "/5");

        youTubePlayerView.initialize(MovieExpandedActivity.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onSuccess");
                youTubePlayer.cueVideo(film.getFilmVideo());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onFailure");
            }
        });

        RemoveFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<Film> query=ParseQuery.getQuery(Film.class);
                query.getInBackground(film.getObjectId(), (object, e) -> {
                    if (e == null) {
                        // Deletes the fetched ParseObject from the database
                        object.deleteInBackground(e2 -> {
                            if(e2==null){
                                Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                                WishListFragment.films.remove(WishListAdapter.adapterposition);
                                WishListFragment.wishListAdapter.notifyItemRemoved(WishListAdapter.adapterposition);
                                HomeFragment.WishListCount--;



                            }else{
                                //Something went wrong while deleting the Object
                                Toast.makeText(getApplicationContext(), "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        //Something went wrong while retrieving the Object
                        Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


}