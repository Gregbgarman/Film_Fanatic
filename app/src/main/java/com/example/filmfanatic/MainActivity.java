package com.example.filmfanatic;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.filmfanatic.fragments.HomeFragment;
import com.example.filmfanatic.fragments.SettingsFragment;
import com.example.filmfanatic.fragments.WishListFragment;
import com.example.filmfanatic.maps.MapsFragment;
import com.example.filmfanatic.maps.PlacesData;
import com.example.filmfanatic.maps.TheaterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements SettingsFragment.SettingsFragmentInterface {

    public static FragmentManager fragmentManager;
    public static CircleImageView ProfilePic;
    public static BottomNavigationView bottomNavigationView;
    public static ParseFile UserImage;
    public static Uri NewProfilePictureUri=null;
    public static ParseUser CurrentUser;
    public static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetTopBars(this,getSupportActionBar(),getWindow());
        actionBar=getSupportActionBar();                        //used in settings fragment

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.itHome:
                        fragment=new HomeFragment();
                        getSupportActionBar().setTitle("CURRENTLY PLAYING");
                        break;
                    case R.id.itWishList:
                        fragment=new WishListFragment();
                        getSupportActionBar().setTitle("WISHLIST");
                        break;
                    case R.id.itMoviesSeen:
                    default:
                       fragment= new MapsFragment();
                        getSupportActionBar().setTitle("NEARBY THEATERS");
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.itHome);        //selects the home fragment at start of program

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        MenuItem menuItem=menu.findItem(R.id.icProfilePic);
        View view= MenuItemCompat.getActionView(menuItem);
        ProfilePic=view.findViewById(R.id.menupic);

                                                                    //getting profilepic
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.setLimit(1);
        query.findInBackground((users, e) -> {
            if (e == null) {

                for(ParseUser user1 : users) {
                    Log.d("User List ",(user1.getUsername()));

                    UserImage=user1.getParseFile("ProfilePicture");
                    CurrentUser=user1;
                    if (UserImage != null) {
                        Glide.with(getApplicationContext()).load(UserImage.getUrl()).into(ProfilePic);
                    }
                    else
                        ProfilePic.setImageResource(R.drawable.reel);

                }
            } else {
                // Something went wrong.
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  //for selecting items on top action bar
        Fragment fragment;
        switch(item.getItemId()) {
            case R.id.icSettings:
                fragment=new SettingsFragment();
                getSupportActionBar().setTitle("SETTINGS");
                break;

            case android.R.id.home:
                setHome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

        View view=bottomNavigationView;         //hides the bottom menu bar with items when go to the settings
        view.setVisibility(View.INVISIBLE);     //fragment

        return true;
    }

    public void LogOut(){
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Error logging out", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
                                //used when hitting back button inside of settings fragment-an activity would be more ideal
    public void setHome(){
        fragmentManager.beginTransaction().replace(R.id.flContainer, new HomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.itHome);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.movieimagesmall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view=bottomNavigationView;
        view.setVisibility(View.VISIBLE);

    }

    public static void SetTopBars(Context context,ActionBar actionBar,Window Topwindow){
       actionBar.setHomeAsUpIndicator(R.drawable.movieimagesmall);     //setting top left icon anytime fxn called
        actionBar.setDisplayHomeAsUpEnabled(true);

                   //setting action bar color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9D58")));

        Window window = Topwindow;                   //setting status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(context,R.color.coolgreen));

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Exit App?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();                 //goes back, which will exit app in this case

            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }
}