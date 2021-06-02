package com.example.filmfanatic.maps;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private String placeName,vicinity,latitude,longitude,place_id,PlacePhoto,PlaceRating,PlaceReviewers,CheckMovieTheater;
    private Double DistanceFromUser;
    private HashMap<String,String> googlePlaceMap;

    private HashMap<String,String> getPlace(JSONObject googlePlaceJson){
        googlePlaceMap=new HashMap<>();
        placeName="NA";
        vicinity="NA";
        latitude="NA";
        longitude="NA";
        place_id="NA";
        PlacePhoto="NA";
        PlaceRating="NA";
        PlaceReviewers="NA";
        CheckMovieTheater="";

        float f[]=new float[10];

                                                        //parsing json data for movie theaters
        try {
            if (!googlePlaceJson.isNull("types")) {

                 JSONArray jsonArray= googlePlaceJson.getJSONArray("types");
                 CheckMovieTheater=jsonArray.get(0).toString();
            }


            if (!googlePlaceJson.isNull("name")) {

                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity=googlePlaceJson.getString("vicinity");
            }
            if (!googlePlaceJson.isNull("rating")) {
                PlaceRating=googlePlaceJson.getString("rating");
            }
            if (!googlePlaceJson.isNull("place_id")) {
                place_id=googlePlaceJson.getString("place_id");
            }

            if (!googlePlaceJson.isNull("user_ratings_total")) {
                PlaceReviewers=googlePlaceJson.getString("user_ratings_total");
            }

            if (!googlePlaceJson.isNull("photos")) {
                PlacePhoto= googlePlaceJson.getJSONArray("photos").getJSONObject(0).getString("photo_reference");

            }

            latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            googlePlaceMap.put("types",CheckMovieTheater);
            googlePlaceMap.put("place_name",placeName);
            googlePlaceMap.put("vicinity",vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);


            googlePlaceMap.put("place_id",place_id);
            googlePlaceMap.put("photo_reference",PlacePhoto);
            googlePlaceMap.put("rating",PlaceRating);
            googlePlaceMap.put("user_ratings_total",PlaceReviewers);

            Location.distanceBetween(MapsFragment.HomeLatLng.latitude,MapsFragment.HomeLatLng.longitude,
                    Double.parseDouble(googlePlaceMap.get("lat")),Double.parseDouble(googlePlaceMap.get("lng")),f);

            DistanceFromUser=f[0]*3.28084/5280;
            googlePlaceMap.put("distance_away",String.valueOf(DistanceFromUser));


        }catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }


    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){
        int count=jsonArray.length();
        List<HashMap<String,String>> placesList=new ArrayList<>();
        HashMap<String,String> placeMap=null;

        for (int i=0;i<count;i++){
            try {
                placeMap=getPlace( (JSONObject)jsonArray.get(i));

                if (placeMap.get("types").contentEquals("movie_theater")) {
                    placesList.add(placeMap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData){
        JSONArray jsonArray=null;
        JSONObject jsonObject;

        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

}


