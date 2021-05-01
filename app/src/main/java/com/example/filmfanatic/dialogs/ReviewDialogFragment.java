package com.example.filmfanatic.dialogs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filmfanatic.Film;
import com.example.filmfanatic.MainActivity;
import com.example.filmfanatic.Movie;
import com.example.filmfanatic.MovieReviewActivity;
import com.example.filmfanatic.R;
import com.example.filmfanatic.Review;
import com.example.filmfanatic.fragments.SettingsFragment;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import static com.parse.Parse.getApplicationContext;


public class ReviewDialogFragment extends DialogFragment {

    Movie ReviewMovie;
    Button btnSubmit;
    Button btnCancel;
    RatingBar ratingBar;
    EditText TextReview;
    TextView tvUsername;

    Float RatingBarValue;

    UpdateAdapterInterface updateAdapterInterface;
    public interface UpdateAdapterInterface{
        public void UpdateAdapter();
    }



    public ReviewDialogFragment(Movie ReviewMovie) {
        this.ReviewMovie=ReviewMovie;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        btnCancel=view.findViewById(R.id.btnCancel);
        ratingBar=view.findViewById(R.id.ratingBar2);
        TextReview=view.findViewById(R.id.etReviewDialog);
        tvUsername=view.findViewById(R.id.tvUserNameDialog);

        tvUsername.setText(ParseUser.getCurrentUser().getUsername().toString());
        ratingBar.setClickable(true);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RatingBarValue=rating;

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review review=new Review();
                review.setReviewTitle(ReviewMovie.getTitle());
                review.setRatingValue(ratingBar.getRating());
                review.setReviewUser(ParseUser.getCurrentUser().getUsername());
                review.setTextReview(TextReview.getText().toString());
                review.setProfileImage(MainActivity.CurrentUser.getParseFile("ProfilePicture"));

                review.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!=null){
                            Toast.makeText(getApplicationContext(), "Error while saving", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(getApplicationContext(), "Review Saved", Toast.LENGTH_SHORT).show();
                    }
                });

                updateAdapterInterface.UpdateAdapter();
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_dialog, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof ReviewDialogFragment.UpdateAdapterInterface){
            updateAdapterInterface = (ReviewDialogFragment.UpdateAdapterInterface) context;
        }

        else{
            throw new RuntimeException(context.toString()+"must implement UpdateAdapterInterface");
        }
    }
}