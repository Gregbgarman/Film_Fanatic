package com.example.filmfanatic.maps;

import android.location.Location;
import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;

import com.example.filmfanatic.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LocalMovieTheaters extends AsyncTask<Object, String, String> {


    private String googlePlacesData;
    private GoogleMap googleMap;
    private String url;
    public static List<HashMap<String,String>> NearbyPlaceList;


    @Override
    protected String doInBackground(Object... objects) {
        googleMap=(GoogleMap)objects[0];
        url=(String)objects[1];


        try {
            googlePlacesData=readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;

    }

    @Override
    protected void onPostExecute(String s) {
        NearbyPlaceList=null;
        DataParser dataParser=new DataParser();
        NearbyPlaceList=dataParser.parse(s);
        showNearbyPlaces(NearbyPlaceList);
        SortPlacesByDistance();
        MapsFragment.BindAdapter(NearbyPlaceList);

    }

    private void showNearbyPlaces(List<HashMap<String,String>> NearbyPlaceList){
        for (int i=0;i<NearbyPlaceList.size();i++){
            MarkerOptions markerOptions=new MarkerOptions();
            HashMap<String,String> googlePlace=NearbyPlaceList.get(i);

            String placeName=googlePlace.get("place_name");
            String vicinity=googlePlace.get("vicinity");
            double lat=Double.parseDouble(googlePlace.get("lat"));
            double lng=Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng=new LatLng(lat,lng);
            markerOptions.position(new LatLng(lat,lng));
            markerOptions.title(placeName);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            googleMap.addMarker(markerOptions);

        }
    }


    public String readUrl(String myurl) throws IOException{
        String data="";
        InputStream inputStream=null;
        HttpsURLConnection urlConnection=null;

        try {
            URL url=new URL(myurl);
            urlConnection=(HttpsURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream=urlConnection.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb=new StringBuffer();

            String line="";                                     //if not null, appends to stringbuffer, and convert to string
            while((line=br.readLine())!=null){
                sb.append(line);
            }

            data=sb.toString();
            br.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void SortPlacesByDistance(){

        HashMap<String,String> HMArray[]=new HashMap[NearbyPlaceList.size()];
        HashMap<String,String> HM;

        for (int i=0;i<NearbyPlaceList.size();i++){
            HMArray[i]=NearbyPlaceList.get(i);
        }

        for (int i=0;i<NearbyPlaceList.size();i++){
            for (int j=0;j<NearbyPlaceList.size()-1;j++){
                if (Double.parseDouble(HMArray[j].get("distance_away"))>Double.parseDouble(HMArray[j+1].get("distance_away"))){
                    HashMap<String,String> temp=HMArray[j];
                    HMArray[j]=HMArray[j+1];
                    HMArray[j+1]=temp;
                }
            }
        }

        NearbyPlaceList.clear();

        for (int i=0;i<HMArray.length;i++){
            NearbyPlaceList.add(HMArray[i]);
        }


    }

}
