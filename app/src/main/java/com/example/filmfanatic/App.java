package com.example.filmfanatic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Film.class);
        ParseObject.registerSubclass(Review.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId( "get from strings" )
                .clientKey("get from strings")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
