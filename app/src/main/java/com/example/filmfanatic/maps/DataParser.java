package com.example.filmfanatic.maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String,String> getPlace(JSONObject googlePlaceJson){
        HashMap<String,String> googlePlaceMap=new HashMap<>();
        String placeName="NA";
        String vicinity="NA";
        String latitude="NA";
        String longitude="NA";
        String reference="NA";
        String place_id="NA";
        String PlacePhoto="NA";
        String PlaceRating="NA";
        String PlaceReviewers="NA";

        String CheckMovieTheater="";


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
            if (!googlePlaceJson.isNull("photo_reference")) {   //needs to be redone-json array
                PlacePhoto=googlePlaceJson.getJSONObject("photos").getString("photo_reference");
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


