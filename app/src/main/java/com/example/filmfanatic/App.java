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
                .applicationId("awtRKAKAa3XBovrGDxcpclc1Hu517uRiCFzMhimo")
                .clientKey("sVQEkWsDoor6tETNcQK1t8yxKoqz49kYlfrhKGrE")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
