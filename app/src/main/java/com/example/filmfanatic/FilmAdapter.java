package com.example.filmfanatic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private Context context;
    private List<Movie> MovieList;

    public FilmAdapter(Context context, List<Movie> MovieList){
        this.context=context;
        this.MovieList=MovieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView= LayoutInflater.from(context).inflate(R.layout.each_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie=MovieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return MovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvRating;
        ImageView ivPoster;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.tvTitle);
            ivPoster=itemView.findViewById(R.id.ivPoster);
            container=itemView.findViewById(R.id.ConstrainedContainerWL);
            tvRating=itemView.findViewById(R.id.tvRating);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.title.toString());
            Float f=(float)movie.getRating()/2;
            tvRating.setText(f.toString() + "/5");

            String imageUrl=movie.getPosterPath();
            Glide.with(context).load(imageUrl).into(ivPoster);

            container.setOnClickListener(new View.OnClickListener() {   //container just allows you to click anywhere on the movie to go
                @Override                                               //to the intent as opposed to having to click on just title etc
                public void onClick(View v) {
                    Intent i = new Intent(context, MovieExpandedActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }
    }

}
