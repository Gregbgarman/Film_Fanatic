package com.example.filmfanatic.maps;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PlacesData extends AsyncTask<Object, String, String> {

    private String url;
    private String PlacesData;

    public static List<HashMap<String,String>> NearbyTheaterData;
    public static HashMap<String,String> WebsitePhone;

    @Override
    protected String doInBackground(Object... objects) {
        url=(String)objects[0];

        try {
            PlacesData=readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

       return PlacesData;

    }

    @Override
    protected void onPostExecute(String s) {

        GoogleDetailsParser googleDetailsParser=new GoogleDetailsParser();
        NearbyTheaterData=googleDetailsParser.parse(s);
        WebsitePhone=googleDetailsParser.getWebsitePhoneNumber(s);
        TheaterActivity.BindAdapter(NearbyTheaterData);

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


}
