package com.example.filmfanatic;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Film")
public class Film extends ParseObject {

    public static final String KEY_FILM_TITLE="FilmTitle";
    public static final String KEY_FILM_DESCRIPTION="FilmDescription";
    public static final String KEY_FILM_RATING="FilmRating";
    public static final String KEY_FILM_POSTER="FilmPoster";
    public static final String KEY_USER="TheUser";
    public static final String KEY_CREATED_AT="createdAt";
    public static final String KEY_VIDEO="FilmVideo";
    public static final String KEY_FILM_POSTER_LARGE="FilmLargePoster";


    public String getFilmLargePoster(){

        return getString(KEY_FILM_POSTER_LARGE);
    }

    public void setFilmLargePoster(String poster){
        put(KEY_FILM_POSTER_LARGE,poster);
    }

    public String getFilmVideo(){
        return getString(KEY_VIDEO);
    }

    public void setFilmVideo(String video){
        put(KEY_VIDEO,video);
    }

    public String getFilmTitle(){
        return getString(KEY_FILM_TITLE);
    }

    public void setFilmTitle(String filmTitle){
        put(KEY_FILM_TITLE,filmTitle);
    }

    public String getFilmDescription(){
        return getString(KEY_FILM_DESCRIPTION);
    }

    public void setFilmDescription(String filmdesc){
        put(KEY_FILM_DESCRIPTION,filmdesc);
    }

    public double getFilmRating(){
        return getDouble(KEY_FILM_RATING);
    }

    public void setFilmRating(double rating){
        put(KEY_FILM_RATING,rating);
    }

    public String getFilmPoster(){
        return getString(KEY_FILM_POSTER);
    }

    public void setFilmPoster(String poster){
        put(KEY_FILM_POSTER,poster);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }


}
