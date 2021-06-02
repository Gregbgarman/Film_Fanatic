package com.example.filmfanatic.maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleDetailsParser {

    private String reviewer,ProfilePhoto,rating,TimeofReview,TheReview;
    private HashMap<String,String> GoogleDetails;

    private HashMap<String,String> getDetail(JSONObject googleDetailJson){
        GoogleDetails=new HashMap<>();
        reviewer="NA";
        ProfilePhoto="NA";
        rating="NA";
        TimeofReview="NA";
        TheReview="NA";

        try {
            if (!googleDetailJson.isNull("author_name")) {
                reviewer=googleDetailJson.getString("author_name");
            }


            if (!googleDetailJson.isNull("profile_photo_url")) {

                ProfilePhoto=googleDetailJson.getString("profile_photo_url").toString();
            }

            if (!googleDetailJson.isNull("rating")) {
                rating=googleDetailJson.getString("rating");
            }

            if (!googleDetailJson.isNull("relative_time_description")) {

                TimeofReview=googleDetailJson.getString("relative_time_description").toString();
            }

            if (!googleDetailJson.isNull("text")) {
                TheReview=googleDetailJson.getString("text");
            }

            GoogleDetails.put("review",TheReview);
            GoogleDetails.put("time",TimeofReview);
            GoogleDetails.put("rating",rating);
            GoogleDetails.put("UserPhoto",ProfilePhoto);
            GoogleDetails.put("reviewer",reviewer);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return GoogleDetails;
    }


    private List<HashMap<String,String>> getDetails(JSONArray jsonArray){
        List<HashMap<String,String>> DetailsList=new ArrayList<>();
        HashMap<String,String> detailMap=null;

        for (int i=0;i<jsonArray.length();i++){
            try {
                detailMap=getDetail( (JSONObject)jsonArray.get(i));
                DetailsList.add(detailMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return DetailsList;
    }

    public List<HashMap<String,String>> parse(String jsonData){
        JSONArray jsonArray=null;
        JSONObject jsonObject;


        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONObject("result").getJSONArray("reviews");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDetails(jsonArray);
    }

    public HashMap<String,String> getWebsitePhoneNumber(String jsondata){
        HashMap<String,String> WebsitePhoneNumber=new HashMap<>();
        JSONObject jsonObject;
        String phonenumber=null;
        String website=null;

        try {
            jsonObject=new JSONObject(jsondata);
            phonenumber=jsonObject.getJSONObject("result").getString("international_phone_number").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject=new JSONObject(jsondata);
            website=jsonObject.getJSONObject("result").getString("website");
        } catch (JSONException e) {
            e.printStackTrace();
        }

            if (phonenumber!=null)
                WebsitePhoneNumber.put("phone_number",phonenumber);
            else
                WebsitePhoneNumber.put("phone_number","NA");

            if (website!=null)
                WebsitePhoneNumber.put("website",website);
            else
                WebsitePhoneNumber.put("website","NA");

        return WebsitePhoneNumber;

    }


}
