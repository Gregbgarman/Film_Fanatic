package com.example.filmfanatic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.filmfanatic.dialogs.ReviewDialogFragment;
import com.example.filmfanatic.fragments.SettingsFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewActivity extends AppCompatActivity implements ReviewDialogFragment.UpdateAdapterInterface {

    public static final String TAG="MovieReviewActivity";

    TextView tvCriticScore;
    TextView tvFanScore;
    TextView tvTitle;
    Button btnWriteReview;
    ImageView backdrop;
    RecyclerView rvReviews;
    ReviewAdapter reviewAdapter;
    List<Review> ReviewList;
    Movie MovieToReview;
    double FanReviewScore=0;

    SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);

        MainActivity.SetTopBars( MovieReviewActivity.this,getSupportActionBar(),getWindow());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);       //setting the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        swipeContainer=findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"fetching new data");
                queryReviews();
            }
        });




        MovieToReview= Parcels.unwrap(getIntent().getParcelableExtra("MovieToReview"));
        tvCriticScore=findViewById(R.id.tvCriticScore);
        tvFanScore=findViewById(R.id.tvFanScore);
        btnWriteReview=findViewById(R.id.btnWriteReview);
        backdrop=findViewById(R.id.ivReviewBackdrop);
        rvReviews=findViewById(R.id.rvReviews);
        tvTitle=findViewById(R.id.tvTitleMRA);



        tvTitle.setText(MovieToReview.getTitle().toString());
        tvCriticScore.setText("Critics Score: " + (MovieToReview.getRating()/2) + "/5");
        String imageUrl=MovieToReview.getBackdropPath();
        Glide.with(getApplicationContext()).load(imageUrl).into(backdrop);

        ReviewList=new ArrayList<>();
        reviewAdapter=new ReviewAdapter(getApplicationContext(),ReviewList);
        rvReviews.setAdapter(reviewAdapter);
        rvReviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        queryReviews();

        btnWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = MovieReviewActivity.this.getSupportFragmentManager();
                ReviewDialogFragment WriteReview=new ReviewDialogFragment(MovieToReview);
                WriteReview.show(fm, "LeavingReview");
            }
        });

    }

    public void queryReviews(){

        ParseQuery<Review> query=ParseQuery.getQuery(Review.class);     //not querying by user anymore,, could be issue
        query.include(Review.KEY_REVIEW_TITLE);
        query.whereEqualTo(Review.KEY_REVIEW_TITLE, MovieToReview.getTitle());
        query.addDescendingOrder(Review.KEY_CREATEDAT);      //orders posts by time
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> theReviews, ParseException e) {
                if (e!=null){
                    return;
                }

                ReviewList.clear();
                FanReviewScore=0;
                ReviewList.addAll(theReviews);
                reviewAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

                for (int i=0;i<theReviews.size();i++){
                    FanReviewScore+= theReviews.get(i).getRatingValue();

                }

                if (theReviews.size()==0)
                    tvFanScore.setText("No reviews");       //default value

                else {
                    Float f = (float) FanReviewScore / theReviews.size();
                    tvFanScore.setText("Fan Score:" + String.format("%.1f", f) + "/5");
                }
            }
        });


    }

    public void UpdateAdapter(){
        ReviewList.clear();
        queryReviews();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  //for selecting back button
        Fragment fragment;
        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return true;
    }


}