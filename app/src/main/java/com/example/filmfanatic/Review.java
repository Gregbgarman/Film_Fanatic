package com.example.filmfanatic;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_CREATEDAT="createdAt";
    public static final String KEY_REVIEW_TITLE="FilmTitle";
    public static final String KEY_TEXTREVIEW="TextReview";
    public static final String KEY_RATING_VALUE="RatingValue";
    public static final String KEY_REVIEW_USER="ReviewUser";
    public static final String KEY_PROFILE_IMAGE="ProfileImage";


    public static String GetTimeStamp (String createdAt){
        return TimeFormatter.getTimeDifference(createdAt).toString();
    }


    public ParseFile getProfileImage(){

        return getParseFile(KEY_PROFILE_IMAGE);
    }

    public void setProfileImage(ParseFile parseFile){

        put(KEY_PROFILE_IMAGE,parseFile);
    }


    public double getRatingValue(){
        return getDouble(KEY_RATING_VALUE);
    }

    public void setRatingValue(double value){
        put(KEY_RATING_VALUE,value);
    }

    public String getReviewTitle(){
        return getString(KEY_REVIEW_TITLE);
    }

    public void setReviewTitle(String title){
        put(KEY_REVIEW_TITLE,title);
    }

    public String getTextReview(){
        return getString(KEY_TEXTREVIEW);
    }

    public void setTextReview(String review){
        put(KEY_TEXTREVIEW,review);
    }

    public String getReviewUser(){
        return getString(KEY_REVIEW_USER);
    }

    public void setReviewUser(String user){
        put(KEY_REVIEW_USER,user);
    }
}
