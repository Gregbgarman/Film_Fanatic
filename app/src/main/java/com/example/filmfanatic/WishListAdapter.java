package com.example.filmfanatic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.parceler.Parcels;

import java.util.List;




public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {



    public static final String TAG="WLAdapter";
    public static int adapterposition;
    private Context context;
    private List<Film> FilmList;

    public WishListAdapter(Context context, List<Film> FilmList){
        this.context=context;
        this.FilmList=FilmList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filmView= LayoutInflater.from(context).inflate(R.layout.each_film, parent, false);    //change
        return new WishListAdapter.ViewHolder(filmView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film film=FilmList.get(position);
        holder.bind(film);
    }

    @Override
    public int getItemCount() {
        return FilmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivPoster;
        ConstraintLayout container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.tvTitleWL);
            ivPoster=itemView.findViewById(R.id.ivPosterWL);
            container=itemView.findViewById(R.id.ConstrainedContainerWL);

        }

        public void bind(Film film) {
            tvTitle.setText(film.getFilmTitle());
            String imageUrl=film.getFilmLargePoster();
            Glide.with(context).load(imageUrl).into(ivPoster);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterposition=getAdapterPosition();
                    Intent i = new Intent(context, WishListActivity.class);
                    i.putExtra("film", Parcels.wrap(film));
                    context.startActivity(i);
                }
            });
        }
    }

}
