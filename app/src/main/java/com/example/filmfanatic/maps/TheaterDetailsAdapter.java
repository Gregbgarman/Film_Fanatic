package com.example.filmfanatic.maps;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmfanatic.R;

import java.util.HashMap;
import java.util.List;

public class TheaterDetailsAdapter extends RecyclerView.Adapter<TheaterDetailsAdapter.ViewHolder> {

    private Context context;
    private List<HashMap<String,String>> DetailList;

    public TheaterDetailsAdapter(Context context, List<HashMap<String,String>> DetailList){
        this.context=context;
        this.DetailList=DetailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.google_review, parent, false);
        return new TheaterDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> hashMap=DetailList.get(position);
        holder.bind(hashMap);
    }

    @Override
    public int getItemCount() {
        return DetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvGoogleReviewerName;
        private TextView tvGoogleReview;
        private TextView tvReviewTime;
        private RatingBar ratingBar;
        private ImageView ivGoogleProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGoogleReviewerName=itemView.findViewById(R.id.tvGoogleReviewerName);
            tvGoogleReview=itemView.findViewById(R.id.tvGoogleReviewerReview);
            ratingBar=itemView.findViewById(R.id.ratingBarGoogleReview);
            ivGoogleProfileImage=itemView.findViewById(R.id.ivGoogleReviewerPhoto);
            tvReviewTime=itemView.findViewById(R.id.tvTimeofReview);

        }

        public void bind(HashMap hashMap) {                 //google reviews

            tvGoogleReviewerName.setText(hashMap.get("reviewer").toString());
            tvGoogleReview.setText(hashMap.get("review").toString());
            ratingBar.setRating(Float.parseFloat(hashMap.get("rating").toString()));
            tvReviewTime.setText(hashMap.get("time").toString());

            Uri uri =Uri.parse(hashMap.get("UserPhoto").toString());
            Glide.with(context).load(uri).into(ivGoogleProfileImage);

        }
    }
}
