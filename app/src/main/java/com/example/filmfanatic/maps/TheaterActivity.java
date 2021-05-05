package com.example.filmfanatic.maps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.filmfanatic.Film;
import com.example.filmfanatic.MainActivity;
import com.example.filmfanatic.R;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class TheaterActivity extends AppCompatActivity {

    Button btnWebsite;
    Button btnCallTheater;
    TextView tvTheaterName;
    TextView tvTheaterAddress;
    ImageView ivTheaterImage;
    RatingBar ratingBar;
    TextView tvReviewcount;
    public static RecyclerView rvGoogleComments;
    public static TheaterDetailsAdapter theaterDetailsAdapter;
    public static Context context;

    Double Latitude;
    Double Longitude;
    String PlaceSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);

        context=this;
        btnWebsite=findViewById(R.id.btnWebsite);
        btnCallTheater=findViewById(R.id.btnPhoneNumber);
        tvTheaterName=findViewById(R.id.tvTheaterName);
        tvTheaterAddress=findViewById(R.id.tvTheaterAddress);
        ivTheaterImage=findViewById(R.id.ivTheaterImage);
        ratingBar=findViewById(R.id.ratingBarTheater);
        tvReviewcount=findViewById(R.id.tvReviewCount);
        rvGoogleComments=findViewById(R.id.rvGoogleComments);

        ratingBar.setClickable(false);

        HashMap HMTheaterInfo= Parcels.unwrap(getIntent().getParcelableExtra("TheaterInfo"));

        Latitude=Double.parseDouble(HMTheaterInfo.get("lat").toString());
        Longitude=Double.parseDouble(HMTheaterInfo.get("lng").toString());

        String photoreference=HMTheaterInfo.get("photo_reference").toString();

        String ImageSearch="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + photoreference +"&key=AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8";

        Uri uri =  Uri.parse( ImageSearch );

        tvTheaterName.setText(HMTheaterInfo.get("place_name").toString());
        tvTheaterAddress.setText(HMTheaterInfo.get("vicinity").toString());
        ratingBar.setRating(Float.parseFloat(HMTheaterInfo.get("rating").toString()));
        tvReviewcount.setText(HMTheaterInfo.get("user_ratings_total").toString() + " reviews");

        if (!photoreference.contentEquals("NA")) {
            Glide.with(this).load(uri).into(ivTheaterImage);
        }


        PlaceSearch="https://maps.googleapis.com/maps/api/place/details/json?place_id="+ HMTheaterInfo.get("place_id") + "&fields=website,reviews,international_phone_number&key=%20AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8";
       // https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJM4ZYb-r17IgRs6KnB6XrM_M&fields=website,opening_hours,reviews,formatted_phone_number,international_phone_number&key=%20AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8

        //PlaceSearch="https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJM4ZYb-r17IgRs6KnB6XrM_M&fields=website,opening_hours,reviews,formatted_phone_number,international_phone_number&key=%20AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8";

        Object dataTransfer[]=new Object[1];
        dataTransfer[0]=PlaceSearch;

        PlacesData placesData=new PlacesData();
        placesData.execute(dataTransfer);


        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(TheaterActivity.this);
                builder.setMessage("Leave app and visit website?");
                builder.setNegativeButton("No",null);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(PlacesData.WebsitePhone.get("website")));
                            startActivity(i);
                    }
                });


                builder.create().show();

            }
        });

        btnCallTheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PlacesData.WebsitePhone.get("phone_number").contains("NA")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TheaterActivity.this);
                    builder.setMessage("No phone number available");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                        }
                    });

                    builder.create().show();

                }

                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(TheaterActivity.this);
                    builder.setMessage("call   " + PlacesData.WebsitePhone.get("phone_number"));
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("tel:"+PlacesData.WebsitePhone.get("phone_number")));
                            startActivity(i);
                        }
                    });

                    builder.setNegativeButton("No", null);
                    builder.create().show();
                }




            }
        });


    }


    public static void BindAdapter(List<HashMap<String,String>> PlaceDetails) {

        theaterDetailsAdapter = new TheaterDetailsAdapter(context ,PlaceDetails);
        rvGoogleComments.setAdapter(theaterDetailsAdapter);
        rvGoogleComments.setLayoutManager(new LinearLayoutManager(context));
        theaterDetailsAdapter.notifyDataSetChanged();


    }



}