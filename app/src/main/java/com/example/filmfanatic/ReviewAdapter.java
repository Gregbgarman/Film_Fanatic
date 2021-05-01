package com.example.filmfanatic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.Parse.getApplicationContext;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private List<Review> ReviewList;

    public ReviewAdapter(Context context, List<Review> ReviewList){
        this.context=context;
        this.ReviewList=ReviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ReviewView= LayoutInflater.from(context).inflate(R.layout.each_review, parent, false);
        return new ViewHolder(ReviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review=ReviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return ReviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvReview;
        RatingBar ReviewBar;
        CircleImageView ivReviewPic;
        TextView username;
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReview= itemView.findViewById(R.id.tvTheReview);
            ReviewBar=itemView.findViewById(R.id.ratingBar);
            ivReviewPic=itemView.findViewById(R.id.ivReviewPic);
            username=itemView.findViewById(R.id.tvUserNameReview);
            tvTime=itemView.findViewById(R.id.tvTime);
        }

        public void bind(Review review) {
            tvReview.setText(review.getTextReview());
            ReviewBar.setRating( (float)review.getRatingValue() );
            username.setText(review.getReviewUser());

            tvTime.setText(review.GetTimeStamp(review.getCreatedAt().toString()));

            if (review.getProfileImage()!=null)
                Glide.with(getApplicationContext()).load(review.getProfileImage().getUrl()).into(ivReviewPic);

            else
                Glide.with(getApplicationContext()).load(R.drawable.reel).into(ivReviewPic);
                                    //change to popcorn at some point


        }
    }

}
