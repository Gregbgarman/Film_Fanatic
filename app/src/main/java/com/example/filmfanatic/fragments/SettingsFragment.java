package com.example.filmfanatic.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.filmfanatic.LoginActivity;
import com.example.filmfanatic.MainActivity;
import com.example.filmfanatic.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;


public class SettingsFragment extends Fragment {

    SettingsFragmentInterface settingsFragmentInterface;

    TextView TheUserName;
    CircleImageView SettingsProfileImage;
    Button btnChangePic;
    Button btnLogOut;
    Uri imageuri;
    ParseFile NewProfilePicture;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TheUserName=view.findViewById(R.id.tvDisplayUserName);
        SettingsProfileImage=view.findViewById(R.id.SettingsProfileImage);
        btnChangePic=view.findViewById(R.id.btnChangePic);
        btnLogOut=view.findViewById(R.id.btnLogOut);

        TheUserName.setText(ParseUser.getCurrentUser().getUsername().toString());
        SettingsProfileImage.setImageResource(R.drawable.reel);

        if (MainActivity.UserImage != null) {
            Glide.with(getApplicationContext()).load(MainActivity.UserImage.getUrl()).into(SettingsProfileImage);
        }
        else
            SettingsProfileImage.setImageResource(R.drawable.popcorn);

        if (MainActivity.NewProfilePictureUri!=null)
            Glide.with(getApplicationContext()).load(MainActivity.NewProfilePictureUri).into(SettingsProfileImage);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("settings fragment", "User clicked btnLogOut");
                settingsFragmentInterface.LogOut();
            }
        });

        btnChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photogallery=new Intent();
                photogallery.setType("image/*");
                photogallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photogallery,"choose picture"),754);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==754 && resultCode==RESULT_OK){
            imageuri=data.getData();
            InputStream inputstream=null;


            Glide.with(getContext()).load(imageuri).into(MainActivity.ProfilePic);
            Glide.with(getContext()).load(imageuri).into(SettingsProfileImage);

            String filename=getFileName(imageuri);
            MainActivity.NewProfilePictureUri=imageuri;

            try {
                inputstream = getContext().getContentResolver().openInputStream(imageuri);
                byte buffer[] = new byte[inputstream.available()];
                inputstream.read(buffer);
                NewProfilePicture = new ParseFile(filename, buffer);
                inputstream.close();

                //update for user here on cloud
                MainActivity.CurrentUser.put("ProfilePicture",NewProfilePicture);
                MainActivity.CurrentUser.saveInBackground();

            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SettingsFragmentInterface){
            settingsFragmentInterface = (SettingsFragmentInterface) context;
        }

        else{
            throw new RuntimeException(context.toString()+
                    "must implement SettingsFragmentInterface");
        }
    }


    public interface SettingsFragmentInterface{
        public void LogOut();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}